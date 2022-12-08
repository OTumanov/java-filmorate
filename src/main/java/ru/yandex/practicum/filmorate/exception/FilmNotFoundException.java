package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends InvalidIdException {
    public FilmNotFoundException(Integer id) {
        super("Фильма с id " + id + " не найдено!");
    }
}
