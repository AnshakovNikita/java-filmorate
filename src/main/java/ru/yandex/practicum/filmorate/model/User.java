package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
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
    @Past
    private LocalDate birthday;
}