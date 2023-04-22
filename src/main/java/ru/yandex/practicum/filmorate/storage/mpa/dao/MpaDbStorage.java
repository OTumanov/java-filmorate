package ru.yandex.practicum.filmorate.storage.mpa.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundExceprion;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        final String getAllSqlQuery =
                "SELECT mpa_id, mpa_name " +
                        "FROM mpa";

        log.info("Запрашиваем все MPA");
        return jdbcTemplate.query(getAllSqlQuery, (rs, rowNum) -> makeMpa(rs, rowNum));
    }

    @Override
    public Mpa getById(Integer id) {
        final String getByIdSqlQuery =
                "SELECT mpa_id, mpa_name " +
                        "FROM mpa " +
                        "WHERE mpa_id = ?";

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(getByIdSqlQuery, id);

        if (!mpaRows.next()) {
            log.info("Нет такого MPA с id {}!", id);
            throw new MpaNotFoundExceprion(id);
        }
        return jdbcTemplate.queryForObject(getByIdSqlQuery, (rs, rowNum) -> makeMpa(rs, rowNum), id);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        Integer mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");

        return new Mpa(mpaId, mpaName);
    }
}
