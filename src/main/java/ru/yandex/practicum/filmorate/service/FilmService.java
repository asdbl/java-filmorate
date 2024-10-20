package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public Film addLike(long userId, long filmId) {
        filmValidation(userId, filmId);
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().add(userId);
        log.info("Added like {} to user {}", filmId, userId);
        return film;
    }

    public Film removeLike(long userId, long filmId) {
        filmValidation(userId, filmId);
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().remove(userId);
        log.info("Removed like from film: {}", film);
        return film;
    }

    public List<Film> getPopularFilms(String limit) {
        int intLimit = Integer.parseInt(limit);
        log.info("Getting popular films from limit {}", intLimit);
        return filmStorage.getAllFilms()
                .stream()
                .filter(f -> f.getLikes() != null)
                .sorted(Comparator.comparingInt(o -> (o.getLikes().size())))
                .limit(intLimit)
                .toList().reversed();
    }

    private void filmValidation(long userId, long filmId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (filmStorage.getFilm(filmId) == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
    }
}
