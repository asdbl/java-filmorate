package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> likesById = new HashMap<>();
    private Long id = 0L;
    private final UserStorage userStorage;

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        likesById.put(film.getId(), new HashSet<>());
        films.put(film.getId(), film);
        log.info("Film added: {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
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
    public Optional<Film> getFilm(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film addLike(long userId, long filmId) {
        if (likesById.containsKey(filmId)) {
            likesById.get(filmId).add(userId);
            log.info("Like added: {}, to film: {}", userId, filmId);
            return getFilm(filmId).orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден."));
        }
        throw new NotFoundException("Фильм с id " + filmId + " не найден.");
    }

    @Override
    public Film removeLike(long userId, long filmId) {
        userStorage.getUser(userId);
        films.get(filmId);
        if (likesById.containsKey(filmId)) {
            likesById.get(filmId).remove(userId);
            log.info("Like removed: {}, to film: {}", userId, filmId);
            return getFilm(filmId).orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден."));
        }
        throw new NotFoundException("Фильм с id " + filmId + " не найден.");
    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        log.info("Get popular films");
        return likesById.keySet().stream()
                .sorted(Comparator.comparingInt(o -> likesById.get(o).size()))
                .flatMap(o -> getFilm(o).stream())
                .limit(limit)
                .toList().reversed();
    }
}
