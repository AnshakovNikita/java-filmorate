package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @NotNull
    @Min(1)
    private long id;

    @NotNull
    @Email
    @Pattern(regexp = "^\\S*$")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    public Set<Long> getFriends() {
        return friends;
    }

    public void setFriends(Long userId) {
        friends.add(userId);
    }
}