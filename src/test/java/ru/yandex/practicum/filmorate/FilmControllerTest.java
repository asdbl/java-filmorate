package ru.yandex.practicum.filmorate;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertFalse;

@SpringBootTest
public class FilmControllerTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void ifFilmNameIsBlankThenThrowException() {
        Film film = new Film();
        film.setDuration(1);
        film.setName("");
        film.setReleaseDate(LocalDate.of(1974, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ifFilmDescriptionLengthIsMoreThen200ThenThrowException() {
        Film film = new Film();
        film.setDuration(1);
        film.setName("Побег из Шоушенка");
        film.setReleaseDate(LocalDate.of(1974, 1, 1));
        film.setDescription("Фильм «Побег из Шоушенка» (1994) — это культовая драма, снятая по мотивам повести Стивена Кинга. В центре сюжета — история Энди Дюфрейна, банкира, несправедливо обвинённого в убийстве жены и приговорённого к пожизненному заключению в тюрьме Шоушенк. Несмотря на жестокость и безнадёжность тюремной жизни, Энди не теряет надежды и находит способы выжить и даже обрести свободу. Фильм получил множество наград и считается одним из лучших в истории кинематографа.");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ifFilmReleaseDateIsBefore1895_12_28ThenThrowException() {
        Film film = new Film();
        film.setDuration(1);
        film.setName("Побег из Шоушенка");
        film.setReleaseDate(LocalDate.of(1774, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ifFilmDurationIsNegativeThenThrowException() {
        Film film = new Film();
        film.setName("Побег из Шоушенка");
        film.setDuration(-1);
        film.setReleaseDate(LocalDate.of(1974, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}