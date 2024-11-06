package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Optional<Film> getFilm(long id);

    Film addLike(long userId, long filmId);

    Film removeLike(long userId, long filmId);

    List<Film> getPopularFilms(int limit);
}
