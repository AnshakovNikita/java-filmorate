package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
}
