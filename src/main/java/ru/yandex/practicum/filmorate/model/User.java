package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    @NotNull
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
}