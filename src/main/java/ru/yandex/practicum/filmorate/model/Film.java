package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @ReleaseDate(year = 1895, month = 12, day = 28, message = "Дата выхода фильма не может быть раньше 1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
