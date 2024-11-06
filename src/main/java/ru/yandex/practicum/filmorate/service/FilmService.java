package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addLike(long userId, long filmId) {
        filmValidation(filmId);
        likeValidation(userId);
        log.info("Adding like to user {}", userId);
        return filmStorage.addLike(userId, filmId);
    }

    public Film removeLike(long userId, long filmId) {
        filmValidation(filmId);
        likeValidation(userId);
        log.info("Removing like from user {}", userId);
        return filmStorage.removeLike(userId, filmId);
    }

    public List<Film> getPopularFilms(int limit) {
        log.info("Getting popular films");
        return filmStorage.getPopularFilms(limit);
    }

    public Film addFilm(Film film) {
        log.info("Adding film {}", film.getName());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Updating film {}", film.getName());
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        log.info("Getting all films");
        return filmStorage.getAllFilms();
    }

    public Film getFilm(long filmId) {
        log.info("Getting film {}", filmId);
        return filmStorage.getFilm(filmId).orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден."));
    }

    private void likeValidation(long userId) {
        if (userStorage.getUser(userId).isEmpty()) {
            log.warn("User not found.");
            throw new NotFoundException("Неверный id пользователя.");
        }
    }

    private void filmValidation(long filmId) {
        if (filmStorage.getFilm(filmId).isEmpty()) {
            log.warn("Film not found.");
            throw new NotFoundException("Неверный id фильма.");
        }
    }
}
