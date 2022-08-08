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

    public User findUser(Long id) {
        return userStorage.findUser(id);
    }

    public User addUser(User user) throws ValidationException {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    public void deleteUsers() {
        userStorage.deleteUsers();
    }

    public void deleteUser(User user) {
        userStorage.deleteUser(user);
    }

    public void addFriend(Long userId, Long friendId) throws ValidationException {
        validId(userId);
        validId(friendId);

        if(userStorage.findUser(userId).getFriends().contains(friendId)) {
            String error = String.format("Пользователи уже друзья");
            log.error(error);
            throw new ValidationException(error);
        }

        userStorage.findUser(userId).setFriends(friendId);
        userStorage.findUser(friendId).setFriends(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        validId(userId);
        validId(friendId);

        if(!userStorage.findUser(userId).getFriends().contains(friendId)) {
            String error = String.format("Пользователи не дружат");
            log.error(error);
            throw new NotFoundException(error);
        }

        userStorage.findUser(userId).getFriends().remove(friendId);
        userStorage.findUser(friendId).getFriends().remove(userId);
    }

    public List<User> usersFriends(Long userId) {

        List<User> result = new ArrayList<>();

        if(userStorage.findUser(userId).getFriends().isEmpty()) {
            String error = String.format("У пользователя нет друзей");
            log.error(error);
            return result;
        }

        for (Long friendsId : userStorage.findUser(userId).getFriends())
            result.add(userStorage.findUser(friendsId));

        return result;
    }

    public List<User> listCommonFriends(Long userID1, Long userID2) {

        validId(userID1);
        validId(userID2);

        List<User> result = new ArrayList<>();

        if(userStorage.findUser(userID1).getFriends().isEmpty()
                || userStorage.findUser(userID2).getFriends().isEmpty()) {
            String error = String.format("у одного из пользователей нет друзей");
            log.error(error);
            return result;
        }

        Set<Long> friendListUser1 = new HashSet<>(userStorage.findUser(userID1).getFriends());
        Set<Long> friendListUser2 = new HashSet<>(userStorage.findUser(userID2).getFriends());
        friendListUser1.retainAll(friendListUser2);

        for (Long userID : friendListUser1)
            result.add(userStorage.findUser(userID));

        if(friendListUser1.isEmpty()) {
            String error = String.format("нет общих друзей");
            log.error(error);
            return result;
        }

        return result;
    }
}
