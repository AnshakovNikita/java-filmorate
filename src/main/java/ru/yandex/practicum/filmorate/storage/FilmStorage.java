package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

public interface FilmStorage {

    HashMap<Long, Film> get();

    Collection<Film> findAll();

    Film add(Film film) throws ValidationException;

    Film find(Long id);

    Film update(Film film) throws ValidationException;

    void deleteAll();

    void delete(Film film);
}
