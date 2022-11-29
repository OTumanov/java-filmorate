package ru.yandex.practicum.filmorate.utils;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.util.regex.Pattern;
import java.time.LocalDate;

@Slf4j
public class UserChecker {

    private final User user;

    public UserChecker(User user) {
        this.user = user;
    }

    public boolean check() {
        return checkLogin() && checkEmail() && checkBirthday();
    }


    private boolean checkLogin() {
        if (user.getLogin().isEmpty() | user.getLogin().contains(" ")) {
            log.warn("Ошибка: логин не может быть пустым и содержать пробелы");
            return false;
        } else return true;
    }

    private boolean checkEmail() {
        if (!Pattern.matches("^\\w+@\\w+\\.\\w+", user.getEmail())) {
            log.warn("Ошибка: электронная почта не может быть пустой и должна содержать символ @");
            return false;
        } else return true;
    }

    private boolean checkBirthday() {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка: дата рождения не может быть в будущем");
            return false;
        } else return true;
    }
}
