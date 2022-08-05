package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class UserController {
    private HashMap<Long, User> users = new HashMap<>();
    private long userId = 1;
    private LocalDate validationBirthday = LocalDate.now();

    public HashMap<Long, User> getUsers() {
        return users;
    }

    public User validation(User user) throws ValidationException {
        if(user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            String error = "email не может быть пустым и должен содержать @";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            String error = "login не может быть пустым и содержать пробелы";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(!user.getBirthday().isBefore(validationBirthday)) {
            String error = "несуществующая дата рождения";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(user.getName().contains(" ")) {
            String error = "В имени не должно быть пробелов";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return user;
    }

    @GetMapping("/users")
    public HashMap<Long, User> findAll() {
        return users;
    }

    @PostMapping(value = "/user")
    public User addUser(@RequestBody User user) throws ValidationException {

        validation(user);

        user.setId(userId);
        users.put(userId, user);
        userId++;

        log.info(String.valueOf(user));
        log.info("Объект /user создан");

        return user;
    }

    @PutMapping(value = "/user")
    public User updateUser(@RequestBody User user) throws ValidationException {

        validation(user);

        if(users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info(String.valueOf(user));
            log.info("Объект /user обновлен");
        } else {
            log.error("Пользователь ненайден.");
            throw new ValidationException("Пользователь ненайден.");
        }


        return user;
    }
}
