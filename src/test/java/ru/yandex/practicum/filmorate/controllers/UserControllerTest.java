package ru.yandex.practicum.filmorate.controllers;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@RequiredArgsConstructor
public class UserControllerTest {
    UserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);
    UserController userController = new UserController(userService);
    User testUser = new User();

    @BeforeEach
    void filmConfig() throws ValidationException {
        testUser.setEmail("student@yandex.ru");
        testUser.setLogin("Student");
        testUser.setName("Никита");
        testUser.setBirthday(LocalDate.of(1992, 3, 21));
        userController.addUser(testUser);
    }

    @Test
    public void shouldBeEmpty() throws ValidationException {
        userController.addUser(testUser);
        assertFalse(userStorage.get().isEmpty());
    }

    @Test
    void emailIsEmpty() {
        testUser.setEmail("");
        Exception exception = assertThrows(ValidationException.class, () -> userController.addUser(testUser));
        assertEquals("email не может быть пустым и должен содержать @", exception.getMessage());

        testUser.setEmail("studentyandex.ru");
        Exception ex = assertThrows(ValidationException.class, () -> userController.addUser(testUser));
        assertEquals("email не может быть пустым и должен содержать @", ex.getMessage());
    }

    @Test
    void loginIsEmpty() {
        testUser.setLogin("");
        Exception exception = assertThrows(ValidationException.class, () -> userController.addUser(testUser));
        assertEquals("login не может быть пустым и содержать пробелы", exception.getMessage());

        testUser.setLogin("Stu dent");
        Exception ex = assertThrows(ValidationException.class, () -> userController.addUser(testUser));
        assertEquals("login не может быть пустым и содержать пробелы", ex.getMessage());
    }

    @Test
    void nameIsEmpty() throws ValidationException {
        User testUserNotName = new User();
        testUserNotName.setEmail("student@yandex.ru");
        testUserNotName.setLogin("Student");
        testUserNotName.setName("");
        testUserNotName.setBirthday(LocalDate.of(1992, 3, 21));
        userController.addUser(testUserNotName);
        assertEquals(testUserNotName.getLogin(), testUserNotName.getName());
    }
}
