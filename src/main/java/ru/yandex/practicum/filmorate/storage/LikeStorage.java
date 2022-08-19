package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikeStorage {

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Long> popular(int count);

}
