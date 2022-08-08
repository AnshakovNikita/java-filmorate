package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film addFilm(Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    public Film findFilm(Long id) {
        return filmStorage.findFilm(id);
    }

    public Film updateFilm(Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilms() {
        filmStorage.deleteFilms();
    }

    public void deleteFilm(Film film) {
        filmStorage.deleteFilm(film);
    }

    public void addLike(Long filmId, Long userId) throws ValidationException {
        if(filmStorage.findFilm(filmId) == null) {
            String error = String.format("Нет фильма с id=%s", filmId);
            log.error(error);
            throw new NotFoundException(error);
        }

        if(filmStorage.findFilm(filmId).getLikes().contains(userId)) {
            String error = "Пользователи уже лайкнул этот фильм";
            log.error(error);
            throw new ValidationException(error);
        }

        filmStorage.findFilm(filmId).setLikes(userId);

    }

    public void deleteLike(Long filmId, Long userId) {
        if(filmStorage.findAllFilms().contains(filmId) || filmId <= 0) {
            String error = String.format("Нет фильма с id=%s", filmId);
            log.error(error);
            throw new NotFoundException(error);
        }

        if(!filmStorage.findFilm(filmId).getLikes().contains(userId)) {
            String error = "Пользователь не лайкал этот фильм";
            log.error(error);
            throw new NotFoundException(error);
        }

        filmStorage.findFilm(filmId).getLikes().remove(userId);
    }

    public List<Film> popular(Integer count) {

        List<Film> films = new ArrayList<>(filmStorage.getFilms().values());

        Collections.sort(films, (film1, film2) -> film2.getLikes().size() - film1.getLikes().size());

        if(count > films.size()) {
            return films.subList(0, films.size());
        }
        
        return films.subList(0, count);
    }
}
