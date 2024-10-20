package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 0L;

    @Override
    public Film addFilm(@RequestBody @Valid Film film) {
        film.setId(++id);
        film.setLikes(new HashSet<>());
        System.out.println(film.getLikes().size());
        films.put(film.getId(), film);
        log.info("Film added: {}", film.getName());
        return film;
    }

    @Override
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

    @Override
    public List<Film> getAllFilms() {
        log.info("Get all films");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(long id) {
        if (!films.containsKey(id)) {
            log.warn("Film not found when get film by id");
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        return films.get(id);
    }
}
