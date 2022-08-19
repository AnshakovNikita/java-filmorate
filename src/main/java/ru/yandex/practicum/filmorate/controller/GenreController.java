package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.storage.DAO.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreDao genreDao;

    @GetMapping
    public Collection<Genre> findAll() {
        return genreDao.findAll();
    }

    @GetMapping("/{genreId}")
    public Optional<Genre> get(@PathVariable("genreId") int id) {
        return genreDao.find(id);
    }

}
