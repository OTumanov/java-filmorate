package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film getFilm(Integer id) {
        final String getFilmSqlQuery =
                "SELECT * " +
                        "FROM films " +
                        "WHERE id = ?";

        log.info("DAO: Запрос фильма с id {} успешно обработан", id);
        return jdbcTemplate.queryForObject(getFilmSqlQuery, (rs, rowNum) -> makeFilm(rs, rowNum), id);
    }


    @Override
    public List<Film> getAllFilms() {
        final String getAllFilmsSqlQuery =
                "SELECT * " +
                        "FROM films";

        log.info("DAO: Запрос всех фильмов успешно обработан");
        return jdbcTemplate.query(getAllFilmsSqlQuery, (rs, rowNum) -> makeFilm(rs, rowNum));
    }

    @Override
    public Film saveFilm(Film film) {
        KeyHolder genId = new GeneratedKeyHolder();

        final String saveFilmSqlQuery =
                "INSERT " +
                        "INTO films (name, description, release_date, duration) " +
                        "VALUES ( ?, ?, ?, ?)";
        final String genreSqlQuery =
                "INSERT" +
                        " INTO film_genre (film_id, genre_id)" +
                        " VALUES ( ?, ?)";
        final String mpaSqlQuery =
                "INSERT" +
                        " INTO mpa_films (film_id, mpa_id)" +
                        " VALUES ( ?, ?)";
        final String findDuplicateSqlQuery =
                "SELECT * " +
                        "FROM film_genre " +
                        "WHERE film_id = ? AND genre_id = ?";

        jdbcTemplate.update(connection -> {
                    final PreparedStatement statement = connection.prepareStatement(saveFilmSqlQuery, new String[]{"id"});

                    statement.setString(1, film.getName());
                    statement.setString(2, film.getDescription());
                    statement.setDate(3, Date.valueOf(film.getReleaseDate()));
                    statement.setLong(4, film.getDuration());
                    return statement;
                },
                genId
        );

        film.setId((Integer) genId.getKey());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                SqlRowSet checkFilmInGenre = jdbcTemplate.queryForRowSet(
                        findDuplicateSqlQuery, film.getId(), genre.getId()
                );

                if (!checkFilmInGenre.next()) {
                    jdbcTemplate.update(genreSqlQuery, film.getId(), genre.getId());
                }
            }
        }

        jdbcTemplate.update(mpaSqlQuery, film.getId(), film.getMpa().getId());
        film.setGenres(getGenreByFilmId(film.getId()));
        film.setMpa(getMpaByFilmId(film.getId()));

        log.info("DAO: Запрос на добавление фильма успешно обработан. Создан новый фильм с id {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        final String updateSqlQuery =
                "UPDATE films " +
                        "SET name = ?, description = ?, release_date = ?, duration = ?" +
                        "WHERE id = ?";
        final String deleteMpaSqlQuery =
                "DELETE " +
                        "FROM mpa_films " +
                        "WHERE film_id = ?";
        final String updateMpaSqlQuery =
                "INSERT " +
                        "INTO mpa_films (film_id, mpa_id) " +
                        "VALUES (?, ?)";
        final String deleteGenreSqlQuery =
                "DELETE" +
                        " FROM film_genre" +
                        " WHERE film_id = ?";
        final String updateGenreSqlQuery =
                "INSERT " +
                        "INTO film_genre (film_id, genre_id)" +
                        " VALUES ( ?, ?)";
        final String findDuplicateSqlQuery =
                "SELECT * " +
                        "FROM film_genre " +
                        "WHERE film_id = ? AND genre_id = ?";

        jdbcTemplate.update(deleteMpaSqlQuery, film.getId());
        jdbcTemplate.update(updateMpaSqlQuery, film.getId(), film.getMpa().getId());

        if (film.getGenres() != null) {
            jdbcTemplate.update(deleteGenreSqlQuery, film.getId());

            for (Genre genre : film.getGenres()) {
                SqlRowSet checkFilmInGenre = jdbcTemplate.queryForRowSet(
                        findDuplicateSqlQuery, film.getId(), genre.getId()
                );

                if (!checkFilmInGenre.next()) {
                    jdbcTemplate.update(updateGenreSqlQuery, film.getId(), genre.getId());
                }
            }
        }

        jdbcTemplate.update(
                updateSqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        film.setMpa(getMpaByFilmId(film.getId()));
        film.setGenres(getGenreByFilmId(film.getId()));

        log.info("DAO: Фильм {} с id {} успешно обновлен", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film deleteFilm(Integer id) {
        Film film = getFilm(id);

        final String deleteSqlQuery =
                "DELETE " +
                        "FROM films " +
                        "WHERE id = ?";
        final String deleteGenreSqlQuery =
                "DELETE " +
                        "FROM film_genre " +
                        "WHERE film_id = ?";
        final String deleteMpaSqlQuery =
                "DELETE " +
                        "FROM mpa_films " +
                        "WHERE film_id = ?";
        final String sqlDeleteLikesQuery =
                "DELETE " +
                        "FROM films_likes " +
                        "3WHERE film_id = ?";

        jdbcTemplate.update(deleteSqlQuery, id);
        jdbcTemplate.update(deleteGenreSqlQuery, id);
        jdbcTemplate.update(deleteMpaSqlQuery, id);
        jdbcTemplate.update(sqlDeleteLikesQuery, id);

        log.info("DAO: Фильм с id {} успешно удален", id);
        return film;
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        final String addLikeSqlQuery =
                "INSERT " +
                        "INTO films_likes (film_id, user_id) " +
                        "VALUES ( ?, ?)";

        jdbcTemplate.update(addLikeSqlQuery, filmId, userId);

        log.info("DAO: Лайк фильму {} от пользователя с id {}", filmId, userId);
        return getFilm(filmId);
    }

    @Override
    public Film removeLike(Integer filmId, Integer userId) {
        final String removeLikeSqlQuery =
                "DELETE " +
                        "FROM films_likes " +
                        "WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(removeLikeSqlQuery, filmId, userId);

        log.info("DAO: Лайк от пользователя с id {} фильму с id {} удалён", userId, filmId);
        return null;
    }

    @Override
    public List<Film> getBestFilms(int count) {
        final String getBestFilmsSqlQuery =
                "SELECT id, name, description, release_date, duration " +
                        "FROM films " +
                        "LEFT JOIN films_likes fl ON films.id = fl.film_id " +
                        "group by films.id, fl.film_id " +
                        "IN (SELECT film_id " +
                        "FROM films_likes) " +
                        "ORDER BY COUNT(fl.film_id) DESC " +
                        "LIMIT ?";

        log.info("DAO: Запрос на ТОП{} фильмов", count);

        return jdbcTemplate.query(getBestFilmsSqlQuery, (rs, rowNum) -> makeFilm(rs, rowNum), count);
    }


    private List<Genre> getGenreByFilmId(int id) {
        final String sqlQuery =
                "SELECT genre.genre_id, GENRE_NAME " +
                        "FROM genre LEFT JOIN film_genre FG on genre.genre_id = FG.GENRE_ID " +
                        "WHERE film_id = ?";

        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeGenre(resultSet, rowNum), id);
    }

    private Mpa getMpaByFilmId(int id) {
        final String sqlQuery =
                "SELECT mpa.mpa_id, mpa_name " +
                        "FROM mpa " +
                        "LEFT JOIN mpa_films ON mpa.mpa_id = mpa_films.mpa_id " +
                        "WHERE film_id = ?";

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        mpaRows.next();

        log.info("DAO: Запрос на MPA фильма с id {}", id);
        return new Mpa(mpaRows.getInt("mpa_id"), mpaRows.getString("mpa_name"));
    }


    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("genre_id");
        String name = resultSet.getString("genre_name");

        log.info("DAO: Запрос на создание жанра {} с id {}", name, id);
        return new Genre(id, name);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {

        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Long duration = rs.getLong("duration");
        LocalDate releaseDate = LocalDate.parse(rs.getString("release_date"));
        Mpa mpa = getMpaByFilmId(rs.getInt("id"));
        List<Genre> genres = getGenreByFilmId(rs.getInt("id"));

        log.info("DAO: Метод создания объекта фильма из бд с id {}", id);
        return new Film(id, name, description, releaseDate, duration, mpa, genres);
    }


    public void checkFilm(Integer id) throws ObjectNotFoundException {
        String checkFilmSqlQuery =
                "SELECT *" +
                        "FROM films " +
                        "WHERE id = ?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(checkFilmSqlQuery, id);

        if (!sqlRowSet.next()) {
            log.warn("Запрошенного фильма с id {} не существует", id);
            throw new ObjectNotFoundException("Запрошенного фильма с id " + id + " не существует");
        }
    }
}