package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

public interface FilmStorage {

    HashMap<Long, Film> getFilms();

    Collection<Film> findAllFilms();

    Film addFilm(Film film) throws ValidationException;

    Film findFilm(Long id);

    Film updateFilm(Film film) throws ValidationException;

    void deleteFilms();

    void deleteFilm(Film film);
}
