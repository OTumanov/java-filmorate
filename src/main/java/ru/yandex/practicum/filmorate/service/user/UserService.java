package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserDbStorage userDbStorage;

    @Override
    public Optional<User> getUser(Integer id) {
        log.info("Сервис: Запрос пользователя с id {}", id);
        checkUser(id);
        return userDbStorage.getUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Сервис: Запрос всех пользователей");
        return userDbStorage.getAllUsers();
    }

    @Override
    public User addUser(User user) {
        log.info("Сервис: Запрос на добавление пользователя с именем {}, логином {} , почтой {} и датой рождения {}",
                user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        validateName(user);
        userDbStorage.saveUser(user);
        return user;
    }

    @Override
    public User removeUser(Integer id) {
        returnUserOrElseThrow(id);
        log.info("Сервис: Запрос на удаление пользователя с id {}", id);
        return userDbStorage.deleteUser(id);
    }

    @Override
    public User updateUser(User user) {
        log.info("Сервис: Запрос на обновление пользователя с id {}. Данные для обновления: имя {}, логин {}, почта {} и дата рождения {}",
                user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        validateName(user);
        checkUser(user.getId());
        return userDbStorage.update(user);
    }

    @Override
    public List<Integer> addAFriend(Integer firstUserId, Integer secondUserId) {
        checkUser(firstUserId);
        checkUser(secondUserId);
        log.info("Сервис: Запрос на добавление дружбы пользователям с id {} и id {}", firstUserId, secondUserId);
        return userDbStorage.addFriendship(firstUserId, secondUserId);
    }

    @Override
    public List<Integer> removeAFriend(Integer userId, Integer friendId) {
        checkUser(userId);
        log.info("Сервис: Запрос на удаление дружбы пользователям с id {} и id {}", userId, friendId);
        return userDbStorage.removeFriendship(userId, friendId);
    }

    @Override
    public Optional<Object> getFriendsOfUser(Integer userId) {
        log.info("Сервис: Запрос на получение друзей пользователя с id {}", userId);
        checkUser(userId);
        return userDbStorage.getAllIdsFriendsByUserId(userId);
    }

    @Override
    public List<User> getCommonFriendsOfUser(Integer firstUserId, Integer secondUserId) {
        log.info("Сервис: Запрос на получение общих друзей пользователей с id {} и id {}", firstUserId, secondUserId);
        checkUser(firstUserId);
        checkUser(secondUserId);
        return userDbStorage.getCommonFriendsList(firstUserId, secondUserId);
    }

    public void checkUser(Integer id) {
        log.info("Сервис: Запрос на проверку пользователя с id {}", id);
        userDbStorage.checkUser(id);
    }

    private void validateName(User user) {
        log.info("Сервис: Запрос на проверку имени пользователя с id {}", user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User returnUserOrElseThrow(Integer userId) {
        return userDbStorage.getUser(userId).orElseThrow(() -> {
            log.info("Сервис: Запрос на проверку пользователя с id {}", userId);
            return new ObjectNotFoundException("Пользователь с id " + userId + " не найден");
        });
    }
}
