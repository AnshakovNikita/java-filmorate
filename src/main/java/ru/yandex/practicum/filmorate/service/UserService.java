package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User find(Long id) {
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

    public void addFriend(Long userId, Long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> usersFriends(Long userId) {
        return userStorage.usersFriends(userId);
    }

    public Collection<User> listCommonFriends(Long userID1, Long userID2) {
        return userStorage.listCommonFriends(userID1, userID2);
    }
}
