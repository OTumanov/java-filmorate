package ru.yandex.practicum.filmorate.storage.genre.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenre(Integer id) {
        log.info("DAO: Запрашиваем жанр с id {}", id);
        final String getByIdSqlQuery =
                "SELECT genre_id, genre_name " +
                        "FROM genre " +
                        "WHERE genre_id = ?";

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(getByIdSqlQuery, id);

        if (!genreRows.next()) {
            log.info("DAO: Нет такого жанра с id {}", id);
            throw new ObjectNotFoundException("Нет такого жанра с id " + id);
        }
        return jdbcTemplate.queryForObject(getByIdSqlQuery, (rs, rowNum) -> makeGenre(rs, rowNum), id);
    }

    @Override
    public List<Genre> getAllGenres() {
        final String findAllSqlQuery =
                "SELECT genre_id, genre_name " +
                        "FROM genre ";

        log.info("DAO: Запрашиваем все существующие жанры");
        return jdbcTemplate.query(findAllSqlQuery, (rs, rowNum) -> makeGenre(rs, rowNum));
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        Integer genreId = rs.getInt("genre_id");
        String genreName = rs.getString("genre_name");

        return new Genre(genreId, genreName);
    }
}