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

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film add(Film film) throws ValidationException {
        return filmStorage.add(film);
    }

    public Film find(Long id) {
        return filmStorage.find(id);
    }

    public Film update(Film film) throws ValidationException {
        return filmStorage.update(film);
    }

    public void deleteAll() {
        filmStorage.deleteAll();
    }

    public void delete(Film film) {
        filmStorage.delete(film);
    }

    public void addLike(Long filmId, Long userId) throws ValidationException {
        if(filmStorage.find(filmId) == null) {
            String error = String.format("Нет фильма с id=%s", filmId);
            log.error(error);
            throw new NotFoundException(error);
        }

        if(filmStorage.find(filmId).getLikes().contains(userId)) {
            String error = "Пользователи уже лайкнул этот фильм";
            log.error(error);
            throw new ValidationException(error);
        }

        filmStorage.find(filmId).setLikes(userId);

    }

    public void deleteLike(Long filmId, Long userId) {
        if(filmStorage.findAll().contains(filmId) || filmId <= 0) {
            String error = String.format("Нет фильма с id=%s", filmId);
            log.error(error);
            throw new NotFoundException(error);
        }

        if(!filmStorage.find(filmId).getLikes().contains(userId)) {
            String error = "Пользователь не лайкал этот фильм";
            log.error(error);
            throw new NotFoundException(error);
        }

        filmStorage.find(filmId).getLikes().remove(userId);
    }

    public List<Film> popular(Integer count) {

        List<Film> films = new ArrayList<>(filmStorage.get().values());

        Collections.sort(films, (film1, film2) -> film2.getLikes().size() - film1.getLikes().size());

        if(count > films.size()) {
            return films.subList(0, films.size());
        }
        
        return films.subList(0, count);
    }
}
