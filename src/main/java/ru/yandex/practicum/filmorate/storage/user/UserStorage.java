package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getUser(Integer id);

    List<User> getAllUsers();

    User saveUser(User user);

    void deleteUser(Integer id);

    void deleteAllUsers();

    Optional<List<Integer>> getAllIdsFriendsByUserId(Integer userId);
}
