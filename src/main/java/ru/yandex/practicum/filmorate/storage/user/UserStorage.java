package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUser(Long id);

    List<User> addFriend(Long userId, Long friendId);

    List<User> removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long user1Id, Long user2Id);
}
