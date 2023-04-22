package ru.yandex.practicum.filmorate.exception;

public class MpaNotFoundExceprion extends InvalidIdException{
    public MpaNotFoundExceprion(Integer id) {
        super("MPA c id " + id + " не найдено!");
    }
}
