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

    @Override
    public HashMap<Long, User> get() {
        return users;
    }

    private long getNextId() {
        return userId++;
    }


    private User validation(User user) throws ValidationException {
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
    public User find(Long id)  {
        if(!users.containsKey(id)) {
            log.error("Пользователь ненайден.");
            throw new NotFoundException("Пользователь ненайден.");
        }
        return users.get(id);
    }

    @Override
    public User add(User user) throws ValidationException {
        validation(user);

        long id = getNextId();

        user.setId(id);
        users.put(user.getId(), user);

        log.info(user + " Объект /user создан");

        return user;
    }

    @Override
    public User update(User user) throws ValidationException {
        validation(user);

        if(users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info(user + " Объект /user обновлен");
        } else {
            log.error("Пользователь ненайден.");
            throw new NotFoundException("Пользователь ненайден.");
        }


        return user;
    }

    @Override
    public void deleteAll() {
        users.clear();
        log.info("список пользователей очищен");
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
        log.info("Объект /user удален");
    }

}
