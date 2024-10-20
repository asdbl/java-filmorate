package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@SpringBootTest
public class UserControllerTest {
    @Autowired
    private UserController userController;
    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void ifEmailIsBlankThrowException() {
        User user = new User();
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ifLoginIsBlankThrowException() {
        User user = new User();
        user.setEmail("abcd@gmail.com");
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ifNameIsBlankThenNameEqualsEmail() {
        User user = new User();
        user.setName("");
        user.setEmail("abcd@gmail.com");
        user.setLogin("abcd");
        user.setBirthday(LocalDate.of(2000, 10, 10));
        userController.createUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void ifBirthdayIsAfterInstantNowThrowException() {
        User user = new User();
        user.setEmail("abcd@gmail.com");
        user.setLogin("abcd");
        user.setBirthday(LocalDate.of(20000, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
