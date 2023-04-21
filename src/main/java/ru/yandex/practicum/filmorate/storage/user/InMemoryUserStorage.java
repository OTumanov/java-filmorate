package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.MakerId;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final MakerId id = new MakerId();

    @Override
    public User update(User user) {
        return user;
    }

    @Override
    public Optional<User> getUser(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User saveUser(User user) {
        if (user.getId() == null) user.setId(id.gen());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(Integer id) {
        users.remove(id);
        return null;
    }

    @Override
    public List<Integer> addFriendship(Integer firstUserId, Integer secondUserId) {
        return null;
    }

    @Override
    public List<Integer> removeFriendship(Integer firstUserId, Integer secondUserId) {
        return null;
    }

    public void deleteAllUsers() {
        users.clear();
    }

    @Override
    public Optional<Object> getAllIdsFriendsByUserId(Integer userId) {
        if (getUser(userId).isPresent()) {
            return Optional.ofNullable(getUser(userId).get().getFriends());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getCommonFriendsList(int firstUserId, int secondUserId) {
        return null;
    }
}
