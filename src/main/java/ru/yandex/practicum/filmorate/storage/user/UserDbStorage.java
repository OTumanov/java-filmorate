package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDbStorage {
    User update(User user);

    User getUser(Integer id);

    List<User> getAllUsers();

    User saveUser(User user);

    User deleteUser(Integer id);

    List<Integer> addFriendship(Integer firstUserId, Integer secondUserId);

    List<Integer> removeFriendship(Integer firstUserId, Integer secondUserId);

    List<User> getAllIdsFriendsByUserId(Integer userId);

    List<User> getCommonFriendsList(int firstUserId, int secondUserId);

    void validate(User user);

    void checkUser(Integer id);
}
