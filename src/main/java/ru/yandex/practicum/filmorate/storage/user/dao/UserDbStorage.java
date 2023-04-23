package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements ru.yandex.practicum.filmorate.storage.user.UserDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getUser(Integer id) {
        final String sqlQuery =
                "SELECT * " +
                        "FROM users " +
                        "WHERE id = ?";

        log.info("DAO: Запрос пользователя с id {} успешно обработан", id);
        return jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowNum) -> makeUser(resultSet, rowNum), id);
    }

    @Override
    public List<User> getAllUsers() {
        final String getAllUsersSqlQuery =
                "SELECT * " +
                        "FROM users";

        log.info("DAO: Запрос всех пользователей успешно обработан");
        return jdbcTemplate.query(getAllUsersSqlQuery, (rs, rowNum) -> makeUser(rs, rowNum));
    }

    @Override
    public User saveUser(User user) {
        KeyHolder genId = new GeneratedKeyHolder();

        final String sqlQuery =
                "INSERT " +
                        "INTO users (email, login, name, birthday) " +
                        "VALUES ( ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
                    final PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});

                    statement.setString(1, user.getEmail());
                    statement.setString(2, user.getLogin());
                    statement.setString(3, user.getName());
                    statement.setDate(4, Date.valueOf(user.getBirthday()));
                    return statement;
                },
                genId
        );

        user.setId((Integer) genId.getKey());

        log.info("DAO: Запрос на добавление пользователя успешно обработан. Создан пользователь с id {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        final String updateSqlQuery =
                "UPDATE users " +
                        "SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                        "WHERE id = ?";

        jdbcTemplate.update(updateSqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());

        log.info("DAO: Пользователь {} с id {} обновлен успешно", user.getName(), user.getId());
        return user;
    }

    @Override
    public User deleteUser(Integer id) {
        final String deleteUserSqlQuery =
                "SELECT * " +
                        "FROM users " +
                        "WHERE id = ?";
        final String deleteSqlQuery =
                "DELETE " +
                        "FROM users " +
                        "WHERE id = ?";

        User user = jdbcTemplate.queryForObject(deleteUserSqlQuery, (resultSet, rowNum) -> makeUser(resultSet, rowNum), id);

        jdbcTemplate.update(deleteSqlQuery, id);
        log.info("DAO: Пользователь с id {} удалён", id);
        return user;
    }

    @Override
    public List<Integer> addFriendship(Integer firstUserId, Integer secondUserId) {
        final String addFriendshipSqlQuery =
                "UPDATE friends " +
                        "SET status = ? " +
                        "WHERE user_id = ? AND friend_id = ?";
        final String sqlWriteQuery =
                "INSERT " +
                        "INTO friends (user_id, friend_id, status ) " +
                        "VALUES (?, ?, ?)";
        final String checkQuery =
                "SELECT * " +
                        "FROM friends " +
                        "WHERE user_id = ? AND friend_id = ?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, firstUserId, secondUserId);

        if (userRows.next()) {
            jdbcTemplate.update(addFriendshipSqlQuery, true, firstUserId, secondUserId);
            log.info("DAO: Пользователь с id {} подтвердил дружбу с пользователем с id {}", firstUserId, secondUserId);
        } else {
            jdbcTemplate.update(sqlWriteQuery, firstUserId, secondUserId, false);
            log.info("DAO: Пользователь с id {} отправил запрос на добавление в друзья пользователю id {}", firstUserId, secondUserId);
        }
        return List.of(firstUserId, secondUserId);
    }


    @Override
    public List<Integer> removeFriendship(Integer firstUserId, Integer secondUserId) {
        final String deleteSqlQuery =
                "DELETE " +
                        "FROM friends " +
                        "WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(deleteSqlQuery, firstUserId, secondUserId);

        log.info("DAO: Пользователь с id {} удалили из друзей пользователя с id {}", firstUserId, secondUserId);
        return List.of(firstUserId, secondUserId);
    }

    @Override
    public List<User> getAllIdsFriendsByUserId(Integer userId) {
        final String getFriendsListByUserIdSqlQuery =
                "SELECT id, email, login, name, birthday " +
                        "FROM users " +
                        "LEFT JOIN friends ON users.id = friends.friend_id " +
                        "WHERE user_id = ?";

        log.info("DAO: Глядим в список  друзей пользователя с id {}", userId);
        return jdbcTemplate.query(getFriendsListByUserIdSqlQuery, (resultSet, rowNum) -> makeUser(resultSet, rowNum), userId);
    }

    @Override
    public List<User> getCommonFriendsList(int firstUserId, int secondUserId) {
        final String getCommonFriendsListSqlQuery =
                "SELECT id, email, login, name, birthday " +
                        "FROM friends " +
                        "LEFT JOIN users ON users.id = friends.friend_id " +
                        "WHERE friends.user_id = ? AND friends.friend_id IN " +
                        "(SELECT friend_id " +
                        "FROM friends " +
                        "LEFT JOIN users ON users.id = friends.friend_id " +
                        "WHERE friends.user_id = ?)";

        log.info("DAO: Глядим на список общих друзей пользователей c id {} и c id {}", firstUserId, secondUserId);
        return jdbcTemplate.query(getCommonFriendsListSqlQuery, (resultSet, rowNum) -> makeUser(resultSet, rowNum), firstUserId, secondUserId);
    }

    @Override
    public void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Поле email не может быть пустым");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Поле login не может быть пустым");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new ValidationException("Поле name не может быть пустым");
        }
        if (user.getBirthday() == null) {
            throw new ValidationException("Поле birthday не может быть пустым");
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        user.setLogin(rs.getString("login"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());

        log.info("DAO: Метод по созданию пользователя из RS. Получили пользвателя с id {}, именем {}, логином {}, почтой {} и днем рождения {}",
                user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());

        return user;
    }

    public void checkUser(Integer id) {
        final String sqlCheckQuery =
                "SELECT * " +
                        "FROM users " +
                        "WHERE id = ?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlCheckQuery, id);

        if (!userRows.next()) {
            log.info("DAO: Запрошенного пользователя с id {} не существует в базе данных", id);
            throw new ObjectNotFoundException("Запрошенного пользователя с id " + id + " не существует в базе данных");
        }
    }
}
