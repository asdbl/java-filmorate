package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        userValidation(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("User created: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
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

    @GetMapping
    public List<User> getUsers() {
        log.info("Get all users");
        return new ArrayList<>(users.values());
    }

    private static void userValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("User name is blank, use email instead");
            user.setName(user.getLogin());
        }
    }
}
