package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends ValidationException
{
    public GenreNotFoundException(Integer id) {
        super("Жанр с id " + id + " не найден");
    }
}
