package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final InMemoryUserStorage userStorage;

    public User addFriend(Long userId, Long friendId) {
        userIdValidation(userId, friendId);
        User user = userStorage.getUser(userId);
        user.getFriends().add(friendId);
        User friend = userStorage.getUser(friendId);
        friend.getFriends().add(userId);
        log.info("Added friend {} to {}", friendId, userId);
        return user;
    }

    public List<User> removeFriend(Long userId, Long friendId) {
        userIdValidation(userId, friendId);
        User user = userStorage.getUser(userId);
        user.getFriends().remove(friendId);
        User friend = userStorage.getUser(friendId);
        friend.getFriends().remove(userId);
        log.info("Removed friend {} from {}", friendId, userId);
        return List.of(user, userStorage.getUser(friendId));
    }

    public List<User> getFriends(Long id) {
        if (userStorage.getUser(id) == null) {
            throw new NotFoundException("Пользователь с id - " + id + " не найден.");
        }
        User user = userStorage.getUser(id);
        List<User> friends = new ArrayList<>();
        user.getFriends().stream()
                .map(userStorage::getUser)
                .forEach(friends::add);
        log.info("Get list of friends");
        return friends;
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) {
        userIdValidation(user1Id, user2Id);
        User user1 = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);
        log.info("Get list of common friends");
        return user1.getFriends().stream()
                .filter(friendId -> user2.getFriends().contains(friendId))
                .map(userStorage::getUser)
                .toList();
    }

    private void userIdValidation(Long userId, Long friendId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с id - " + userId + " не найден.");
        }
        if (userStorage.getUser(friendId) == null) {
            throw new NotFoundException("Пользователь с id - " + friendId + " не найден.");
        }
    }
}
