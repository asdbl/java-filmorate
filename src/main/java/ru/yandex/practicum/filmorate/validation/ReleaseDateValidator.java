package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    private LocalDate annotationDate;

    @Override
    public void initialize(ReleaseDate releaseDate) {
        this.annotationDate = LocalDate.of(releaseDate.year(), releaseDate.month(), releaseDate.day());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.isAfter(annotationDate);
    }
}