package ru.yandex.practicum.filmorate.utils;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmChecker {
    private final Film film;
    private static final int LENGTH_DESCRIPTION = 200;
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);


    public FilmChecker(Film film) {
        this.film = film;
    }

    public boolean check() {
        return checkName() & checkLengthDescription() & checkReleaseDate() & checkDuration();
    }

    private boolean checkName() {
        if (film.getName().isBlank()) {
            log.warn("Ошибка: название не может быть пустым");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkLengthDescription() {
        if (film.getDescription().length() >= LENGTH_DESCRIPTION) {
            log.warn("Ошибка: максимальная длина описания — 200 символов");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkReleaseDate() {
        if (film.getReleaseDate().isAfter(DATE) & film.getReleaseDate().isBefore(LocalDate.now())) {
            return true;
        } else {
            log.warn("Ошибка: дата релиза — не раньше 28 декабря 1895 года");
            return false;
        }
    }

    private boolean checkDuration() {
        if (film.getDuration() < 0) {
            log.warn("Ошибка: продолжительность фильма должна быть положительной");
            return false;
        } else {
            return true;
        }
    }
}
