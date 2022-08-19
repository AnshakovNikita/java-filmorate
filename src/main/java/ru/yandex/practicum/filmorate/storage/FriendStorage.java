package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    void addFriend(long fromUserId, long toUserId);

    List<User> usersFriends(long id);

    List<User> listCommonFriends(long id, long otherId);

    void deleteFriends(long id, long otherId);

}
