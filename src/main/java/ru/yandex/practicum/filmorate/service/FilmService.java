package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final GenreDao genreDao;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film add(Film film) throws ValidationException {
        Film newFilm = filmStorage.add(film);
        film.setId(newFilm.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreDao.recordGenresToFilm(film.getId(), genre.getId());
            }
        }
        return film;
    }

    public Film find(Long id) {
        return filmStorage.find(id);
    }

    public Film update(Film film) throws ValidationException {
        genreDao.deleteGenresToFilm(film.getId());
        filmStorage.update(film);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreDao.recordGenresToFilm(film.getId(), genre.getId());
            }
        }
        return film;
    }

    public boolean delete(long id) {
        return filmStorage.delete(id);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> popular(Integer count) {
        return filmStorage.popular(count);
    }
}
