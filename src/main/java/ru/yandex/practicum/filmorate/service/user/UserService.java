package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService implements UserServiceInterface {
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User getUser(Integer id) {
        return returnUserOrElseThrow(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User addUser(User user) {
        userStorage.saveUser(user);
        return user;
    }

    @Override
    public void removeUser(Integer id) {
        returnUserOrElseThrow(id);
        userStorage.deleteUser(id);
    }

    @Override
    public User updateUser(User user) {
        returnUserOrElseThrow(user.getId());
        userStorage.saveUser(user);
        return user;
    }

    @Override
    public void addAFriend(Integer userId, Integer friendId) {
        returnUserOrElseThrow(userId).addAFriend(friendId);
        returnUserOrElseThrow(friendId).addAFriend(userId);
    }

    @Override
    public void removeAFriend(Integer userId, Integer friendId) {
        returnUserOrElseThrow(userId).removeAFriend(friendId);
        returnUserOrElseThrow(friendId).removeAFriend(userId);
    }

    @Override
    public List<User> getFriendsOfUser(Integer userId) {

        List<User> friendsOfUser = new ArrayList<>();

        for (User user : userStorage.getAllUsers()) {
            if (returnUserOrElseThrow(userId).getFriends().contains(user.getId())) {
                Optional<User> listFriendsOfUser = userStorage.getUser(user.getId());
                friendsOfUser.add(listFriendsOfUser.get());
            }
        }
        return friendsOfUser;
    }

    @Override
    public List<User> getCommonFriendsOfUser(Integer userId, Integer otherId) {

        Set<User> friendsOfUser = new HashSet<>();
        Set<User> friendsOfOtherUser = new HashSet<>();

        for (User user : userStorage.getAllUsers()) {
            if (returnUserOrElseThrow(userId).getFriends().contains(user.getId())) {
                Optional<User> listFriendsOfUser = userStorage.getUser(user.getId());
                friendsOfUser.add(listFriendsOfUser.get());
            }
        }

        for (User otherUser : userStorage.getAllUsers()) {
            if (returnUserOrElseThrow(otherId).getFriends().contains(otherUser.getId())) {
                Optional<User> listFriendsOfOtherUser = userStorage.getUser(otherUser.getId());
                friendsOfOtherUser.add(listFriendsOfOtherUser.get());
            }
        }

        friendsOfUser.retainAll(friendsOfOtherUser);
        return new ArrayList<>(friendsOfUser);
    }

    private User returnUserOrElseThrow(Integer userId) {
        return userStorage.getUser(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
