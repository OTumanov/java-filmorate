package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class ReleaseDateValidator implements ConstraintValidator<Film.FilmReleaseDateCheck, LocalDate> {
    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        return releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));
    }
}
