package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Size(max = 200, message = "Мксимальня длина описания - 200 символов")
    private String description;
    @NotNull(message = "Дата выхода не может быть пустой")
    @ReleaseDate(year = 1895, month = 12, day = 28, message = "Дата выхода фильма не может быть раньше 1895-12-28")
    private LocalDate releaseDate;
    @NotNull(message = "Длительность фильма не может быть пустой")
    @Positive(message = "Длительность фильм не может быть отрицательной")
    private int duration;
}
