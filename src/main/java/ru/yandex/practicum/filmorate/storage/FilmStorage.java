package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> findAll();

    Film add(Film film) throws ValidationException;

    Film find(Long id);

    Film update(Film film) throws ValidationException;

    boolean delete(long id);

    Film addLike(long id, long userId);

    Film deleteLike(long id, long userId);

    List<Film> popular(int count);

}
