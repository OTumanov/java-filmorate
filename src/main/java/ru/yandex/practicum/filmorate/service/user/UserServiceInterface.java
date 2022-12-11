package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

 interface UserServiceInterface {
    User getUser(Integer id);

     List<User> getAllUsers();

     User addUser(User user);

     void removeUser(Integer id);

     User updateUser(User user);

     void addAFriend(Integer userId, Integer friendId);

     void removeAFriend(Integer userId, Integer friendId);

     List<User> getFriendsOfUser(Integer userId);

     List<User> getCommonFriendsOfUser(Integer userId, Integer otherId);
}
