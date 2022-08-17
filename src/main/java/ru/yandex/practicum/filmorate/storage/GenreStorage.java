package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Collection<Genre> findAll();

    Optional<Genre> find(int id);

    void deleteGenresToFilm(long id);

    void recordGenresToFilm(long film_id, int genre_id);

    List<Genre> getGenresToFilm(Long id);
}
