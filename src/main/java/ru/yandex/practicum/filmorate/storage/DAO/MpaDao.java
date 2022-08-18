package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Qualifier
@Slf4j
@RequiredArgsConstructor
public class MpaDao implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa(resultSet.getInt("rating_id"), resultSet.getString("rating_name"));
        return mpa;
    }

    @Override
    public Collection<Mpa> findAll() {
        String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Mpa find(Long id) {
        if(id > 0 && id < 6) {
            String sqlQuery = "select *" +
                    "from MPA where RATING_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        } else {
            String error = "неизвестный MPA";
            log.error(error);
            throw new NotFoundException(error);
        }
    }
}
