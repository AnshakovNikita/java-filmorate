package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();

    User find(Long id);

    User add(User user) throws ValidationException;

    User update(User user) throws ValidationException;

    boolean delete(long id);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    Collection<User> usersFriends(Long userId);

    Collection<User> listCommonFriends(Long userID1, Long userID2);

}
