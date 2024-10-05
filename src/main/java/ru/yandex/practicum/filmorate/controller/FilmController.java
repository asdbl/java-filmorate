package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 0L;

    @PostMapping()
    public Film addFilm(@RequestBody @Valid Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Film added: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (film.getId() == null) {
            log.warn("Film id is null");
            throw new ValidationException("Id должен быть указан");
        }
        Long filmId = film.getId();
        if (films.containsKey(filmId)) {
            films.put(filmId, film);
            log.info("Film updated");
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
