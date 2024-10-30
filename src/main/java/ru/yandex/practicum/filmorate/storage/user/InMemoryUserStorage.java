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
        userValidation(user);
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
        Optional<User> optionalUser = Optional.ofNullable(users.get(id));
        return optionalUser.orElseThrow(() -> new NotFoundException("Введен неверный id пользователя"));
    }

    @Override
    public List<User> addFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        if (userId.equals(friendId)) {
            log.warn("User id is same as friendId when adding friend");
            throw new NotFoundException("id пользователья не может совпадать с id друга.");
        }
        friendsByUserId.get(userId).add(friendId);
        log.trace("friend with id {} added to user with id {}", friendId, userId);
        friendsByUserId.get(friendId).add(userId);
        return getFriends(userId);
    }

    @Override
    public List<User> removeFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        if (userId.equals(friendId)) {
            log.warn("User id is same as friendId when removing friend");
            throw new NotFoundException("id пользователья не может совпадать с id друга.");
        }
        friendsByUserId.get(userId).remove(friendId);
        log.trace("friend with id {} removed from user with id {}", friendId, userId);
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
                .map(this::getUser)
                .toList();
    }

    @Override
    public List<User> getCommonFriends(Long user1Id, Long user2Id) {
        User user1 = getUser(user1Id);
        User user2 = getUser(user2Id);
        log.trace("Get common friends of user1 {} and user2 {}", user1Id, user2Id);
        return friendsByUserId.get(user1Id).stream()
                .filter(friendId -> friendsByUserId.get(user2Id).contains(friendId))
                .map(this::getUser)
                .toList();
    }

    private static void userValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("User name is blank, use email instead");
            user.setName(user.getLogin());
        }
    }
}
