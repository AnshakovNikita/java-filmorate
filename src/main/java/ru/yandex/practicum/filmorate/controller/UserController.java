package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") long id) {
        return userService.find(id);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.add(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.update(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(@Valid @PathVariable("userId") Long userId,
                           @Valid @PathVariable("friendId") Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteFriend(@Valid @PathVariable("userId") Long userId,
                             @Valid @PathVariable("friendId") Long friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> usersFriends(@Valid @PathVariable("userId") Long userId) {
        return userService.usersFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> listCommonFriends(@Valid @PathVariable("userId") Long userId,
                                        @Valid @PathVariable("otherId") Long otherId) {
        return userService.listCommonFriends(userId, otherId);
    }
}
