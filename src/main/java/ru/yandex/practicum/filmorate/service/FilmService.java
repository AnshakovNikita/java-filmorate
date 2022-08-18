package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.DAO.GenreDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Qualifier
public class FilmService {

    private final FilmStorage filmStorage;
    private final GenreDao genreDao;
    private final LikeStorage likeStorage;


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

    public Film addLike(long filmId, long userId) {

        if(filmStorage.find(filmId) == null) {
            String error = "неизвестный фильм";
            log.error(error);
            throw new NotFoundException(error);
        }

        likeStorage.addLike(filmId, userId);
        return filmStorage.find(filmId);
    }

    public Film deleteLike(Long filmId, Long userId) {
        if(filmId > 0 && userId > 0) {
            likeStorage.deleteLike(filmId, userId);
        } else {
            String error = "неизвестный id";
            log.error(error);
            throw new NotFoundException(error);
        }
            return filmStorage.find(filmId);
    }

    public List<Film> popular(Integer count) {
        List<Long> mostPopularFilmsId = likeStorage.popular(count);
        return mostPopularFilmsId.stream()
                .map(filmStorage::find)
                .collect(Collectors.toList());
    }
}
