package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @NotNull
    private long id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    @NotBlank
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Min(1)
    private long duration;

    private Set<Long> likes = new HashSet<>();

    public Set<Long> getLikes() {
        return likes;
    }

    public void setLikes(Long like) {
        likes.add(like);
    }
}
