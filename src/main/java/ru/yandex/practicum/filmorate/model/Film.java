package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder
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

    private int rate;

    @Min(1)
    @Max(5)
    @JsonProperty(value = "mpa")
    private Mpa rating;

    private LinkedHashSet<Genre> genres;

}
