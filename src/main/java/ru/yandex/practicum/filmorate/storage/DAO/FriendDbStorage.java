package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier
@Slf4j
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

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
    public void addFriend(long userId, long friendId) {

        String sqlQuery = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);

    }

    @Override
    public List<User> usersFriends(long userId) {
        String sql = "select u.* from USERS as u " +
                "left join FRIENDS F on u.USER_ID = F.FRIEND_ID " +
                "WHERE f.USER_ID = ?";

        List<User> result = jdbcTemplate.query(sql, this::mapRowToUser, userId);

        return result;
    }

    @Override
    public List<User> listCommonFriends(long id, long otherId) {
        String sql = "SELECT * From USERS where USER_ID IN (SELECT FRIEND_ID " +
                "FROM FRIENDS where USER_ID = ?) " +
                "AND USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = ?)";

        List<User> result = jdbcTemplate.query(sql, this::mapRowToUser, id, otherId);

        return result;
    }

    @Override
    public void deleteFriends(long id, long otherId) {
        String sql = "DELETE FROM FRIENDS " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, id, otherId);
    }
}
