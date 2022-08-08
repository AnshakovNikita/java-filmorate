package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;


@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private HashMap<Long, User> users = new HashMap<>();
    private long userId = 1;
    private LocalDate validationBirthday = LocalDate.now();

    public HashMap<Long, User> getUsers() {
        return users;
    }

    private long getNextId() {
        return userId++;
    }


    public User validation(User user) throws ValidationException {
        if(user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            String error = "email не может быть пустым и должен содержать @";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            String error = "login не может быть пустым и содержать пробелы";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(!user.getBirthday().isBefore(validationBirthday)) {
            String error = "несуществующая дата рождения";
            log.warn(error);
            throw new ValidationException(error);
        }

        if(user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findUser(Long id)  {
        if(!users.containsKey(id)) {
            log.error("Пользователь ненайден.");
            throw new NotFoundException("Пользователь ненайден.");
        }
        return users.get(id);
    }

    @Override
    public User addUser(User user) throws ValidationException {
        validation(user);

        long id = getNextId();

        user.setId(id);
        users.put(user.getId(), user);

        log.info(String.valueOf(user));
        log.info("Объект /user создан");

        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        validation(user);

        if(users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info(String.valueOf(user));
            log.info("Объект /user обновлен");
        } else {
            log.error("Пользователь ненайден.");
            throw new NotFoundException("Пользователь ненайден.");
        }


        return user;
    }

    @Override
    public void deleteUsers() {
        users.clear();
        log.info("список пользователей очищен");
    }

    @Override
    public void deleteUser(User user) {
        log.info(String.valueOf(user));
        users.remove(user.getId());
        log.info("Объект /user удален");
    }

}
