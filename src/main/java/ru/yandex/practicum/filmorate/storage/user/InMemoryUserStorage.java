package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.MakeId;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final MakeId id = new MakeId();

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
    public void deleteUser(Integer id) {
        users.remove(id);
    }
    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    @Override
    public Optional<List<Integer>> getAllFriendsOfUser(Integer userId) {
        if (getUser(userId).isPresent()) {
            return Optional.of(new ArrayList<>(users.get(userId).getFriends()));
        } else {
            return Optional.empty();
        }
    }
}
