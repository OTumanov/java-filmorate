package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.UserChecker;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsersTest {
    static User user = new User(
            "otumanov@gmail.com",
            "otumanov",
            "Oleg Tumanov",
            LocalDate.of(1984, 5, 11));


    @AfterEach
    public void user() {
        user = new User(
                "otumanov@gmail.com",
                "otumanov",
                "Oleg Tumanov",
                LocalDate.of(1984, 5, 11));
    }

    @Test
    public void checkUserTest() {
        UserChecker userChecker = new UserChecker(user);
        assertTrue(userChecker.check());
    }

    @Test
    public void checkEmailTest() {
        UserChecker userChecker = new UserChecker(user);
        user.setEmail("emailgmail.com");
        assertFalse(userChecker.check());
        user.setEmail("");
        assertFalse(userChecker.check());
    }

    @Test
    public void checkLoginTest() {
        UserChecker userChecker = new UserChecker(user);
        user.setLogin("na me");
        assertFalse(userChecker.check());
        user.setLogin("");
        assertFalse(userChecker.check());
        user.setLogin("otumanov");
        assertTrue(userChecker.check());
    }

    @Test
    public void checkBirthdayTest() {
        UserChecker userChecker = new UserChecker(user);
        user.setBirthday(LocalDate.of(2222, 11, 11));
        assertFalse(userChecker.check());
        user.setBirthday(LocalDate.of(1984, 5, 11));
        assertTrue(userChecker.check());
    }
}
