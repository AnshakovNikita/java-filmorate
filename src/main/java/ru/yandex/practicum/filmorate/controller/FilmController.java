package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film get(@PathVariable("filmId") Long id) {
        return filmService.find(id);
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.update(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@Valid @PathVariable("filmId") Long filmId,
                        @Valid @PathVariable("userId") Long userId) throws ValidationException {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@Valid @PathVariable("filmId") Long filmId,
                             @Valid @PathVariable("userId") Long userId) throws ValidationException {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.popular(count);
    }
}
