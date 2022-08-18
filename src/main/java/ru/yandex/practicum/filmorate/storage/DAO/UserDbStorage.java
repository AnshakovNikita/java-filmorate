package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private LocalDate validationBirthday = LocalDate.now();

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

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("user_email"))
                .login(resultSet.getString("user_login"))
                .name(resultSet.getString("user_name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User find(long id) {
        if(id > 0) {
            String sqlQuery = "select *" +
                    "from USERS where USER_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } else {
            String error = "неизвестный юзер";
            log.error(error);
            throw new NotFoundException(error);
        }
    }

    @Override
    public User add(User user) throws ValidationException {

        validation(user);

        String sql = "insert into USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) throws ValidationException {

        validation(user);

        if(user.getId() > 0) {
            String sqlQuery = "update USERS set " +
                    "USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? " +
                    "where USER_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return user;
        } else {
            String error = "неизвестный юзер";
            log.error(error);
            throw new NotFoundException(error);
        }
    }
    @Override
    public boolean delete(long id) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public User addFriend(long id, long friendId) {
        return null;
    }

    @Override
    public User deleteFriend(long id, long friendId) {
        return null;
    }

    @Override
    public List<User> usersFriends(long id) {
        return null;
    }

    @Override
    public List<User> listCommonFriends(long id, long otherId) {
        return null;
    }
}
