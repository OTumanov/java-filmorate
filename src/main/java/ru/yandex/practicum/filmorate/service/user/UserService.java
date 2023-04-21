package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.dao.UserDbStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserDbStorage userDbStorage;

    @Override
    public User getUser(Integer id) {
        return returnUserOrElseThrow(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    @Override
    public User addUser(User user) {
        userDbStorage.saveUser(user);
        return user;
    }

    @Override
    public void removeUser(Integer id) {
        returnUserOrElseThrow(id);
        userDbStorage.deleteUser(id);
    }

    @Override
    public User updateUser(User user) {
        returnUserOrElseThrow(user.getId());
        userDbStorage.saveUser(user);
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

        for (User user : userDbStorage.getAllUsers()) {
            if (returnUserOrElseThrow(userId).getFriends().contains(user.getId())) {
                Optional<User> listFriendsOfUser = userDbStorage.getUser(user.getId());
                friendsOfUser.add(listFriendsOfUser.get());
            }
        }
        return friendsOfUser;
    }

    @Override
    public List<User> getCommonFriendsOfUser(Integer userId, Integer otherId) {

        Set<User> friendsOfUser = new HashSet<>();
        Set<User> friendsOfOtherUser = new HashSet<>();

        for (User user : userDbStorage.getAllUsers()) {
            if (returnUserOrElseThrow(userId).getFriends().contains(user.getId())) {
                Optional<User> listFriendsOfUser = userDbStorage.getUser(user.getId());
                friendsOfUser.add(listFriendsOfUser.get());
            }
        }

        for (User otherUser : userDbStorage.getAllUsers()) {
            if (returnUserOrElseThrow(otherId).getFriends().contains(otherUser.getId())) {
                Optional<User> listFriendsOfOtherUser = userDbStorage.getUser(otherUser.getId());
                friendsOfOtherUser.add(listFriendsOfOtherUser.get());
            }
        }

        friendsOfUser.retainAll(friendsOfOtherUser);
        return new ArrayList<>(friendsOfUser);
    }

    public User returnUserOrElseThrow(Integer userId) {
        return userDbStorage.getUser(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
