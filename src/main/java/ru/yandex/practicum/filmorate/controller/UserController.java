package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Long, User> users = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static Long id = 0L;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        userValidation(user);
        user.setId(++id);
        users.put(user.getId(), user);
        LOG.info("User created: {}", user);
        return user;
    }

    private static void userValidation(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            LOG.warn("User email address is invalid");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            LOG.warn("User login is invalid");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            LOG.warn("User name is blank, use email instead");
            user.setName(user.getEmail());
        }
        if (user.getBirthday().isAfter(Instant.now())) {
            LOG.warn("User birthday is invalid");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() != null) {
            if (users.containsKey(user.getId())) {
                userValidation(user);
                users.put(user.getId(), user);
                LOG.info("User updated: {}", user);
                return user;
            }
        }
        LOG.warn("User id is invalid");
        throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
    }

    @GetMapping
    public List<User> getUsers() {
        LOG.info("Get all users");
        return new ArrayList<>(users.values());
    }


}
