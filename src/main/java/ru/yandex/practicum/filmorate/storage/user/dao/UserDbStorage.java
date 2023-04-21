package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        final String getAllUsersSqlQuery =
                "SELECT * " +
                        "FROM users";

        log.info("Запрос всех пользователей");
        return jdbcTemplate.query(getAllUsersSqlQuery, this::makeUser);
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

        log.info("Пользователь с id {} сохранен", user.getId());
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

        log.info("Пользователь с id {} обновлен", user.getId());
        return user;
    }

    @Override
    public Optional<User> getUser(Integer id) {
        final String sqlQuery =
                "SELECT * " +
                        "FROM users " +
                        "WHERE id = ?";

        log.info("Запрос пользователя с id {}", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowNum) -> makeUser(resultSet, rowNum), id));
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
        log.info("Пользователь с id {} удалён", id);
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
            log.info("user с id = {} подтвердил дружбу с user id = {}", firstUserId, secondUserId);
        } else {
            jdbcTemplate.update(sqlWriteQuery, firstUserId, secondUserId, false);
            log.info("user с id = {} подписался на user id = {}", firstUserId, secondUserId);
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

        log.info("Пользователю с id {} удалили из друзей пользователя с id {}", firstUserId, secondUserId);
        return List.of(firstUserId, secondUserId);
    }

    @Override
    public Optional<Object> getAllIdsFriendsByUserId(Integer userId) {
        final String getFriendsListByUserIdSqlQuery =
                "SELECT id, email, login, name, birthday " +
                        "FROM users " +
                        "LEFT JOIN friends ON users.id = friends.friend_id " +
                        "WHERE user_id = ?";

        log.info("Глядим в список  друзей пользователя {}", userId);
        return Optional.of(jdbcTemplate.query(getFriendsListByUserIdSqlQuery, (resultSet, rowNum) -> makeUser(resultSet, rowNum), userId));
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

        log.info("Глядим на список общих друзей пользователей {} и {}", firstUserId, secondUserId);
        return jdbcTemplate.query(getCommonFriendsListSqlQuery, (resultSet, rowNum) -> makeUser(resultSet, rowNum), firstUserId, secondUserId);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
//
//    public void checkUser(int id) {
//        final String sqlCheckQuery = "SELECT * FROM users WHERE id = ?";
//        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlCheckQuery, id);
//        if (!userRows.next()) {
//            log.info("user с id = {} не найден.", id);
//            throw new UserNotFoundException(id);
//        }
//    }
}
