package ru.yandex.practicum.filmorate.DAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier
@Slf4j
@RequiredArgsConstructor
public class GenreDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public Genre mapRowToGenre(ResultSet rs) throws SQLException {
        Genre out = Genre.builder().
                id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
        return out;
    }

    @Override
    public Collection<Genre> findAll() {
        String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public Optional<Genre> find(int id) {
        if(id > 0 && id < 7) {
            String sqlQuery = "SELECT *" +
                    "FROM GENRES WHERE GENRE_ID = ?";
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToGenre(rs), id).stream().findAny();
        } else {
            String error = "неизвестный жанр";
            log.error(error);
            throw new NotFoundException(error);
        }
    }

    @Override
    public void deleteGenresToFilm(long id) {
        String sqlQuery = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
    @Override
    public void recordGenresToFilm(long film_id, int genre_id) {
        String sql = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, film_id, genre_id);
    }

    @Override
    public List<Genre> getGenresToFilm(Long id) {
        String sql =
                "SELECT GENRES.GENRE_ID, GENRES.GENRE_NAME " +
                        "FROM FILMS_GENRES " +
                        "JOIN GENRES " +
                        "ON GENRES.GENRE_ID = FILMS_GENRES.GENRE_ID " +
                        "WHERE FILMS_GENRES.FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs), id);
    }
}
