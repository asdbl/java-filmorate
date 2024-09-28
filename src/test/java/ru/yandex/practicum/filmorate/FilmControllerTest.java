package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.Instant;

@SpringBootTest
public class FilmControllerTest {

    static FilmController filmController = new FilmController();

    @Test
    public void ifFilmNameIsBlankThenThrowException() {
        Film film = new Film();
        film.setDuration(Duration.ofMinutes(1));
        film.setName("");
        film.setReleaseDate(Instant.parse("1994-12-03T00:00:00.00Z"));
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        Assertions.assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    public void ifFilmDescriptionLengthIsMoreThen200ThenThrowException() {
        Film film = new Film();
        film.setDuration(Duration.ofMinutes(1));
        film.setName("Побег из Шоушенка");
        film.setReleaseDate(Instant.parse("1994-12-03T00:00:00.00Z"));
        film.setDescription("Фильм «Побег из Шоушенка» (1994) — это культовая драма, снятая по мотивам повести Стивена Кинга. В центре сюжета — история Энди Дюфрейна, банкира, несправедливо обвинённого в убийстве жены и приговорённого к пожизненному заключению в тюрьме Шоушенк. Несмотря на жестокость и безнадёжность тюремной жизни, Энди не теряет надежды и находит способы выжить и даже обрести свободу. Фильм получил множество наград и считается одним из лучших в истории кинематографа.");
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        Assertions.assertEquals("Описание фильма не может быть больше 200", exception.getMessage());
    }

    @Test
    public void ifFilmReleaseDateIsBefore1895_12_28ThenThrowException() {
        Film film = new Film();
        film.setDuration(Duration.ofMinutes(1));
        film.setName("Побег из Шоушенка");
        film.setReleaseDate(Instant.parse("1895-11-28T00:00:00.00Z"));
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        Assertions.assertEquals("Дата релиза фильма не может быть раньше 1895-12-28T00:00:00Z", exception.getMessage());
    }

    @Test
    public void ifFilmDurationIsNegativeThenThrowException() {
        Film film = new Film();
        film.setDuration(Duration.ofMinutes(1));
        film.setName("Побег из Шоушенка");
        film.setDuration(Duration.ofMinutes(-1));
        film.setReleaseDate(Instant.parse("1994-12-03T00:00:00.00Z"));
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        Assertions.assertEquals("Длительность фильма не может быть отрицательной", exception.getMessage());
    }
}
