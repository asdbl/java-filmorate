package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReleaseDate {
    int year();

    int month();

    int day();

    String message() default "{ReleaseDate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
