package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Qualifier
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User find(long id) {
        if (userStorage.find(id) == null) {
            String error = "неизвестный фильм";
            log.error(error);
            throw new NotFoundException(error);
        }

        return userStorage.find(id);
    }

    public User add(User user) throws ValidationException {
        return userStorage.add(user);
    }

    public User update(User user) throws ValidationException {
        return userStorage.update(user);
    }

    public boolean delete(Long id) {
        return userStorage.delete(id);
    }

    public User addFriend(long id, long friendId) {
        User user = userStorage.find(id);
        User friend = userStorage.find(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        friendStorage.addFriend(id, friendId);

        return userStorage.addFriend(id, friendId);
    }

    public User deleteFriend(long userId, long friendId) {
        User user = userStorage.find(userId);
        User friend = userStorage.find(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        friendStorage.deleteFriends(userId, friendId);
        return userStorage.deleteFriend(userId, friendId);
    }

    public List<User> usersFriends(long userId) {
        User user = userStorage.find(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return friendStorage.usersFriends(userId);
    }

    public List<User> listCommonFriends(long id, long otherId) {
        User user = userStorage.find(id);
        User otherUser = userStorage.find(otherId);
        if (user == null || otherUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return friendStorage.listCommonFriends(id, otherId);
    }
}
