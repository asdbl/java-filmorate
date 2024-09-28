package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    private Instant releaseDate;
    private Duration duration;
}
