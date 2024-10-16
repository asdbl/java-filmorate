package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(max = 200, message = "Мксимальня длина описания - 200 символов")
    private String description;
    @ReleaseDate(year = 1895, month = 12, day = 28, message = "Дата выхода фильма не может быть раньше 1895-12-28")
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильм не может быть отрицательной")
    private int duration;
}
