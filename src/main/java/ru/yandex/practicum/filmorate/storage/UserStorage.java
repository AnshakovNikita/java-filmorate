package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public interface UserStorage {

    Collection<User> findAll();

    User find(Long id);

    User add(User user) throws ValidationException;

    User update(User user) throws ValidationException;

    void deleteAll();

    void delete(User user);

    HashMap<Long, User> get();
}
