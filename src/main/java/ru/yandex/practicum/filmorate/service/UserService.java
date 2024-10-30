package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserStorage userStorage;

    public List<User> addFriend(Long userId, Long friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    public List<User> removeFriend(Long userId, Long friendId) {
        return userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) {
        return userStorage.getCommonFriends(user1Id, user2Id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(long userId) {
        return userStorage.getUser(userId);
    }
}
