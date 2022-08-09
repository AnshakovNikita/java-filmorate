package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public void validId(Long id) {
        if(userStorage.findAll().contains(id) || id <= 0) {
            String error = String.format("Нет пользователя с id=%s", id);
            log.error(error);
            throw new NotFoundException(error);
        }
    }

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

    public void deleteAll() {
        userStorage.deleteAll();
    }

    public void delete(User user) {
        userStorage.delete(user);
    }

    public void addFriend(Long userId, Long friendId) throws ValidationException {
        validId(userId);
        validId(friendId);

        if(userStorage.find(userId).getFriends().contains(friendId)) {
            String error = "Пользователи уже друзья";
            log.error(error);
            throw new ValidationException(error);
        }

        userStorage.find(userId).setFriends(friendId);
        userStorage.find(friendId).setFriends(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        validId(userId);
        validId(friendId);

        if(!userStorage.find(userId).getFriends().contains(friendId)) {
            String error = "Пользователи не дружат";
            log.error(error);
            throw new NotFoundException(error);
        }

        userStorage.find(userId).getFriends().remove(friendId);
        userStorage.find(friendId).getFriends().remove(userId);
    }

    public List<User> usersFriends(Long userId) {

        List<User> result = new ArrayList<>();

        if(userStorage.find(userId).getFriends().isEmpty()) {
            String error = "У пользователя нет друзей";
            log.error(error);
            return result;
        }

        for (Long friendsId : userStorage.find(userId).getFriends())
            result.add(userStorage.find(friendsId));

        return result;
    }

    public List<User> listCommonFriends(Long userID1, Long userID2) {

        validId(userID1);
        validId(userID2);

        List<User> result = new ArrayList<>();

        if(userStorage.find(userID1).getFriends().isEmpty()
                || userStorage.find(userID2).getFriends().isEmpty()) {
            String error = "у одного из пользователей нет друзей";
            log.error(error);
            return result;
        }

        Set<Long> friendListUser1 = new HashSet<>(userStorage.find(userID1).getFriends());
        Set<Long> friendListUser2 = new HashSet<>(userStorage.find(userID2).getFriends());
        friendListUser1.retainAll(friendListUser2);

        for (Long userID : friendListUser1)
            result.add(userStorage.find(userID));

        if(friendListUser1.isEmpty()) {
            String error = "нет общих друзей";
            log.error(error);
            return result;
        }

        return result;
    }
}
