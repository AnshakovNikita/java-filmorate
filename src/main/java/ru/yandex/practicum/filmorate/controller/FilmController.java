package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {

    private HashMap<Long, Film> films = new HashMap();
    private long filmId = 1;
    private LocalDate validationReleaseDate = LocalDate.of(1895, 1, 28);

    public HashMap<Long, Film> getFilms() {
        return films;
    }

    private long getNextId() {
        return filmId++;
    }



    public Film validation(Film film) throws ValidationException {

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

            return film;
        }

    @GetMapping("/films")
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {

        validation(film);

        long id = getNextId();

        film.setId(id);
        films.put(film.getId(), film);
        filmId++;

        log.info(String.valueOf(film));
        log.info("Объект /film создан");

        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {

        validation(film);

        if(films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info(String.valueOf(film));
            log.info("Объект /user обновлен");
        } else {
            throw new ValidationException("Нет такого фильма.");
        }

        return film;
    }
}
