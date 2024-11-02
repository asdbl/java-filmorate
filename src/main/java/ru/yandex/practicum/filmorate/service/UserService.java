package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public List<User> addFriend(Long userId, Long friendId) {
        friendValidation(userId);
        friendValidation(friendId);
        log.info("Adding friend {} to user {}", friendId, userId);
        return userStorage.addFriend(userId, friendId);
    }

    public List<User> removeFriend(Long userId, Long friendId) {
        friendValidation(userId);
        friendValidation(friendId);
        log.info("Removing friend {} from user {}", friendId, userId);
        return userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long id) {
        friendValidation(id);
        log.info("Getting friends {}", id);
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) {
        friendValidation(user1Id);
        friendValidation(user2Id);
        log.info("Getting common friends {}", user1Id);
        return userStorage.getCommonFriends(user1Id, user2Id);
    }

    public User createUser(User user) {
        userValidation(user);
        log.info("Creating user {}", user.getId());
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        userValidation(user);
        log.info("Updating user {}", user.getId());
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(long userId) {
        log.info("Getting user {}", userId);
        return userStorage.getUser(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден."));
    }

    private void userValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("User name is blank, use email instead");
            user.setName(user.getLogin());
        }
    }

    private void friendValidation(long userId) {
        if (userStorage.getUser(userId).isEmpty()) {
            log.warn("User not found {}", userId);
            throw new NotFoundException("Неверный id пользователя.");
        }
    }
}
