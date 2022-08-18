package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier
@Slf4j
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long filmId, long userId) {

        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.debug("Добавлен лайк фильму с id: {} пользователем с id: {}", filmId, userId);

    }

    @Override
    public void deleteLike(long filmId, long userId) {

        String sql = "DELETE FROM LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
        log.debug("Удалён лайк фильму с id: {} пользователем с id: {}", filmId, userId);

    }

    @Override
    public List<Long> popular(int count) {

        String sql = "select f.*, m.RATING_ID, m.RATING_NAME, COUNT(l.USER_ID) AS count from FILMS AS f " +
                "LEFT JOIN LIKES AS l ON f.FILM_ID = l.FILM_ID " +
                "LEFT JOIN MPA AS m ON f.RATING_ID = m.RATING_ID " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY count DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, this::mapRowToIdList, count);
    }

    private long mapRowToIdList(ResultSet resultSet, int id) throws SQLException {
        return resultSet.getLong("film_id");
    }

}
