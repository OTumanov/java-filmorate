package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends InvalidIdException{
    public UserNotFoundException(Integer id) {
        super("Пользователя с id " + id + " не найдено!");
    }
}
