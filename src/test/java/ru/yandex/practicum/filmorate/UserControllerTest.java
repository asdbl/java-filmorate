package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {
    static UserController userController = new UserController();

    @Test
    public void ifEmailIsBlankThrowException() {
        User user = new User();
        user.setEmail("");
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    public void ifLoginIsBlankThrowException() {
        User user = new User();
        user.setEmail("abcd@gmail.com");
        user.setLogin("");
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals("Логин не может быть пустым", exception.getMessage());
    }

    @Test
    public void ifNameIsBlankThenNameEqualsEmail() {
        User user = new User();
        user.setName("");
        user.setEmail("abcd@gmail.com");
        user.setLogin("abcd");
        user.setBirthday(LocalDate.of(2000, 10, 10));
        userController.createUser(user);
        Assertions.assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void ifBirthdayIsAfterInstantNowThrowException() {
        User user = new User();
        user.setEmail("abcd@gmail.com");
        user.setLogin("abcd");
        user.setBirthday(LocalDate.of(20000, 10, 10));
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }
}
