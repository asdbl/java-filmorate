package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film addLike(long userId, long filmId) {
        return filmStorage.addLike(userId, filmId);
    }

    public Film removeLike(long userId, long filmId) {
        return filmStorage.removeLike(userId, filmId);
    }

    public List<Film> getPopularFilms(int limit) {
        return filmStorage.getPopularFilms(limit);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(long filmId) {
        return filmStorage.getFilm(filmId);
    }
}
