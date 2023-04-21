package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User update(User user);

    Optional<User> getUser(Integer id);

    List<User> getAllUsers();

    User saveUser(User user);

    User deleteUser(Integer id);

    List<Integer> addFriendship(Integer firstUserId, Integer secondUserId);

    List<Integer> removeFriendship(Integer firstUserId, Integer secondUserId);

    Optional<Object> getAllIdsFriendsByUserId(Integer userId);

    List<User> getCommonFriendsList(int firstUserId, int secondUserId);
}
