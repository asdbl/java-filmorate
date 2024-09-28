package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    public static final int max_description_length = 200;
    public static final Instant min_release_date = Instant.parse("1895-12-28T00:00:00Z");
    private final HashMap<Long, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private static Long id = 0L;

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        filmValidation(film);
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    private static void filmValidation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Film name is blank");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null) {
            if (film.getDescription().length() > max_description_length) {
                log.warn("Film description is too long");
                throw new ValidationException("Описание фильма не может быть больше " + max_description_length + " символов");
            }
        }
        if (film.getReleaseDate().isBefore(min_release_date)) {
            log.warn("Film release date is before minimum");
            throw new ValidationException("Дата релиза фильма не может быть раньше " + min_release_date);
        }
        if (film.getDuration().isNegative()) {
            log.warn("Film duration is negative");
            throw new ValidationException("Длительность фильма не может быть отрицательной");
        }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            log.warn("Film id is null");
            throw new ValidationException("Id должен быть указан");
        }
        Long filmId = film.getId();
        if (films.containsKey(filmId)) {
            filmValidation(film);
            films.put(filmId, film);
            log.info("Film added");
            return film;
        }
        log.warn("Film not found");
        throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
    }


    @GetMapping
    public List<Film> getFilms() {
        log.info("Get all films");
        return new ArrayList<>(films.values());
    }
}
