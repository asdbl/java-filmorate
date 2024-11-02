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
    private final Map<Long, Set<Long>> friendsByUserId = new HashMap<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        friendsByUserId.put(user.getId(), new HashSet<>());
        users.put(user.getId(), user);
        log.info("User created: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() != null) {
            if (users.containsKey(user.getId())) {
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
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            log.warn("User id is same as friendId when adding friend");
            throw new NotFoundException("id пользователья не может совпадать с id друга.");
        }
        friendsByUserId.get(userId).add(friendId);
        log.info("friend with id {} added to user with id {}", friendId, userId);
        friendsByUserId.get(friendId).add(userId);
        return getFriends(userId);
    }

    @Override
    public List<User> removeFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            log.warn("User id is same as friendId when removing friend");
            throw new NotFoundException("id пользователья не может совпадать с id друга.");
        }
        friendsByUserId.get(userId).remove(friendId);
        log.info("friend with id {} removed from user with id {}", friendId, userId);
        friendsByUserId.get(friendId).remove(userId);
        return getFriends(userId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        if (!friendsByUserId.containsKey(userId)) {
            log.warn("User id is invalid when getting friends");
            throw new NotFoundException("Пользователь с таким id не найден.");
        }
        return friendsByUserId.get(userId).stream()
                .flatMap(o -> getUser(o).stream())
                .toList();
    }

    @Override
    public List<User> getCommonFriends(Long user1Id, Long user2Id) {
        log.info("Get common friends of user1 {} and user2 {}", user1Id, user2Id);
        return friendsByUserId.get(user1Id).stream()
                .filter(friendId -> friendsByUserId.get(user2Id).contains(friendId))
                .flatMap(o -> getUser(o).stream())
                .toList();
    }
}
