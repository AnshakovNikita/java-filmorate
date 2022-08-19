package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    Collection<User> findAll();

    User find(long id);

    User add(User user) throws ValidationException;

    User update(User user) throws ValidationException;

    boolean delete(long id);

    User addFriend(long id, long friendId);

    User deleteFriend(long id, long otherId);

    List<User> listCommonFriends(long id, long otherId);

    List<User> usersFriends(long id);

}
