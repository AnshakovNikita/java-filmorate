package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();

    User findUser(Long id);

    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    void deleteUsers();

    void deleteUser(User user);
}
