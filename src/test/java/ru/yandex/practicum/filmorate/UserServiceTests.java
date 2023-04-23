package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
public class UserServiceTests {
    private final UserService userService;
    private final User user1 = new User(
            1,
            "test1@mail.ru",
            "LoginUser1",
            "NameUser1",
            LocalDate.of(1984, 5, 11));

    private final User user2 = new User(
            1,
            "test2@mail.ru",
            "LoginUser2",
            "NameUser2",
            LocalDate.of(1984, 5, 11));


    @Test
    void addUserAndGetUserTest() {
        userService.addUser(user1);

        assertEquals(Optional.of(user1), userService.getUser(1));
    }

    @Test
    void addUserAndGetAllUsersTest() {
        assertEquals(new ArrayList<>(), userService.getAllUsers());

        userService.addUser(user1);

        assertEquals(List.of(user1), userService.getAllUsers());
    }

    @Test
    void deleteUserTest() {
        userService.addUser(user1);
        userService.removeUser(1);

        assertEquals(new ArrayList<>(), userService.getAllUsers());
    }

    @Test
    void updateUserTest() {
        userService.addUser(user1);
        user1.setName("updatedUserName");
        userService.updateUser(user1);

        assertEquals(Optional.of(user1), userService.getUser(1));
    }

    @Test
    void addFriendsTest() {
        userService.addUser(user1);
        user1.setName("userFriend1");
        userService.addUser(user2);

        assertEquals(Optional.of(new ArrayList<>()), userService.getFriendsOfUser(1));

        userService.addAFriend(1, 2);

        assertEquals(Optional.of(List.of(user2)), userService.getFriendsOfUser(1));
    }

        @Test
    void removeFriendsTest() {
        userService.addUser(user1);
        user1.setName("userFriend1");
        userService.addUser(user2);
        userService.addAFriend(1, 2);

        assertEquals(Optional.of(List.of(user2)), userService.getFriendsOfUser(1));

        userService.removeAFriend(1, 2);

        assertEquals(Optional.of(new ArrayList<>()), userService.getFriendsOfUser(1));
    }

    @Test
    void getCommonFriendsTest() {
        userService.addUser(user1);
        user1.setName("userFriend1");
        userService.addUser(user2);
        user2.setName("userFriend2");
        userService.addUser(user1);

        assertEquals(new ArrayList<>(), userService.getCommonFriendsOfUser(1, 2));

        userService.addAFriend(1, 3);
        userService.addAFriend(2, 3);

        assertEquals(List.of(user1), userService.getCommonFriendsOfUser(1, 2));
    }

    @Test
    public void testUpdateUserWithId9999NotFound() {
        user1.setId(999);
        Assertions.assertThatThrownBy(() -> userService.updateUser(user1)).isInstanceOf(ObjectNotFoundException.class);
    }
}