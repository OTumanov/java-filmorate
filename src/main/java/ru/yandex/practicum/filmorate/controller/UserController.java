package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        log.info("Контроллер: запрос пользователя с id {}", id);
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Контроллер: запрос всех пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Контроллер: запрос на добавление пользователя с именем {}, логином {} , почтой {} и датой рождения {}",
                user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Контроллер: запрос на обновление пользователя с id {}. Данные для обновления: имя {}, логин {}, почта {} и дата рождения {}",
                user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteById(@PathVariable Integer id) {
        log.info("Контроллер: запрос на удаление пользователя с id {}", id);
        return userService.removeUser(id);
    }

    @PutMapping("/{firstUserId}/friends/{secondUserId}")
    public List<Integer> addFriends(@PathVariable Integer firstUserId, @PathVariable Integer secondUserId) {
        log.info("Контроллер: запрос на добавление дружбы пользователям с id {} и id {}", firstUserId, secondUserId);
        return userService.addAFriend(firstUserId, secondUserId);
    }

    @DeleteMapping("/{firstUserId}/friends/{secondUserId}")
    public List<Integer> deleteFriends(@PathVariable Integer firstUserId, @PathVariable Integer secondUserId) {
        log.info("Контроллер: запрос на удаление дружбы пользователям с id {} и id {}", firstUserId, secondUserId);
        return userService.removeAFriend(firstUserId, secondUserId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        log.info("Контроллер: запрос друзей пользователя с id {}", id);
        return userService.getFriendsOfUser(id);
    }

    @GetMapping("/{firstUserId}/friends/common/{secondUserId}")
    public List<User> getCommonFriends(@PathVariable Integer firstUserId, @PathVariable Integer secondUserId) {
        log.info("Контроллер: запрос общих друзей пользователей с id {} и id {}", firstUserId, secondUserId);
        return userService.getCommonFriendsOfUser(firstUserId, secondUserId);
    }
}
