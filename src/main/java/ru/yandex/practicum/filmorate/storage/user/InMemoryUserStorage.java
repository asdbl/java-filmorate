package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        userValidation(user);
        user.setId(++id);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("User created: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() != null) {
            if (users.containsKey(user.getId())) {
                userValidation(user);
                users.put(user.getId(), user);
                log.info("User updated: {}", user);
                return user;
            }
        }
        log.warn("User id is invalid");
        throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Get all users");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        log.warn("User not found");
        throw new NotFoundException("Пользователь с id - " + id + " не найден.");
    }

    private static void userValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("User name is blank, use email instead");
            user.setName(user.getLogin());
        }
    }
}
