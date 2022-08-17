package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.DAO.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaDao mpaDao;

    public MpaController(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @GetMapping
    public Collection<Mpa> findAll() {
        return mpaDao.findAll();
    }

    @GetMapping("/{mpaId}")
    public Mpa get(@PathVariable("mpaId") Long id) {
        return mpaDao.find(id);
    }
}
