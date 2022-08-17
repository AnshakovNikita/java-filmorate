package ru.yandex.practicum.filmorate.DAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final GenreDao genreDao;

    private final JdbcTemplate jdbcTemplate;

    private LocalDate validationReleaseDate = LocalDate.of(1895, 1, 28);

    private Film validation(Film film) throws ValidationException {

        if(film.getName() == null || film.getName().isEmpty()) {
            String error = "Название фильма не может быть пустым";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(film.getDescription().length() > 200) {
            String error = "Описание не должно превышать 200 символов";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(film.getReleaseDate().isBefore(validationReleaseDate)) {
            String error = "Дата релиза не может быть раньше 28 декабря 1895 года";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(film.getDuration() < 0) {
            String error = "Продолжительность фильма не может быть отрицательной";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(film.getRating() == null) {
            String error = "MPA не должен быть пустым";
            log.warn(error);
            throw new ValidationException(error);
        }

        return film;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {

        Mpa mpa = new Mpa(resultSet.getInt("rating_id"),
                resultSet.getString("rating_name"));

        Film film = Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("film_description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .rating(mpa)
                .build();
        film.setGenres(new LinkedHashSet<>(genreDao.getGenresToFilm(film.getId())));
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select * from FILMS AS f INNER JOIN mpa AS m ON f.RATING_ID = m.RATING_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film add(Film film) throws ValidationException {

        validation(film);

        String sql = "insert into FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASEDATE, DURATION, RATING_ID)" +
                " values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getRating().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film find(Long id) {
        if(id > 0) {

            String sqlQuery = "select *" +
                    "from FILMS INNER JOIN mpa AS m ON FILMS.RATING_ID = m.RATING_ID where FILM_ID = ?";

            SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

            if(userRows.next()) {
                return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
            } else {
                String error = "неизвестный фильм";
                log.error(error);
                return null;
            }
        } else {
            String error = "неизвестный фильм";
            log.error(error);
            throw new NotFoundException(error);
        }
    }

    @Override
    public Film update(Film film) throws ValidationException {

        validation(film);

        if(film.getId() > 0) {
            String sqlQuery = "update FILMS set " +
                    "FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATING_ID = ? " +
                    "where FILM_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getRating().getId(),
                    film.getId());
            return film;
        } else {
            String error = "неизвестный фильм";
            log.error(error);
            throw new NotFoundException(error);
        }
    }

    @Override
    public boolean delete(long id) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

        String sql = "DELETE FROM LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        int result = jdbcTemplate.update(sql, filmId, userId);
        if (result != 1) {
            String error = "ошибка удаления";
            log.error(error);
            throw new NotFoundException(error);
        }
    }

    @Override
    public Collection<Film> popular(Integer count) {

        String sql = "select f.*, m.RATING_ID, m.RATING_NAME, COUNT(l.USER_ID) AS count from FILMS AS f " +
        "LEFT JOIN LIKES AS l ON f.FILM_ID = l.FILM_ID " +
        "LEFT JOIN MPA AS m ON f.RATING_ID = m.RATING_ID " +
        "GROUP BY f.FILM_ID " +
        "ORDER BY count DESC " +
        "LIMIT 10";

        List<Film> films = jdbcTemplate.query(sql, this::mapRowToFilm);

        if(count > films.size()) {
            return films.subList(0, films.size());
        }

        return films.subList(0, count);
    }

}
