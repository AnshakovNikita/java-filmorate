package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@RequiredArgsConstructor
public class FilmControllerTest {

    FilmStorage filmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(filmStorage);
    FilmController filmController = new FilmController(filmService);
    Film testFilm = new Film();

    @BeforeEach
    void filmConfig() throws ValidationException {
        testFilm.setName("Терминатор");
        testFilm.setDescription("Судный день");
        testFilm.setReleaseDate(LocalDate.of(1992, 3, 21));
        testFilm.setDuration(120);
        filmController.add(testFilm);
    }


    @Test
    public void shouldBeEmpty() {
        assertFalse(filmStorage.get().isEmpty());
    }

    @Test
    void nameIsEmpty() {
        testFilm.setName("");
        Exception exception = assertThrows(ValidationException.class, () -> filmController.add(testFilm));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void earlyDate() {
        testFilm.setReleaseDate(LocalDate.of(1800, 3, 21));
        Exception exception = assertThrows(ValidationException.class, () -> filmController.add(testFilm));
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void negativeDuration() {
        testFilm.setDuration(-10);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.add(testFilm));
        assertEquals("Продолжительность фильма не может быть отрицательной", exception.getMessage());
    }
}