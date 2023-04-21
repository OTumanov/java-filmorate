package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.exception.InvalidParameterCounter;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.response.ErrorResponse;
import ru.yandex.practicum.filmorate.storage.user.dao.UserDbStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserDbStorage userDbStorage;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Запрос: POST /users");
        userDbStorage.saveUser(user);
        return user;
    }

    @GetMapping(value = "/{id}")
    public Optional<User> getUser(@PathVariable(value = "id") int userId) {
        log.info("Запрос: GET /users/{}", userId);
        return userDbStorage.getUser(userId);
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Запрос: GET /users");
        return userDbStorage.getAllUsers();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Запрос: PUT /users");
        userDbStorage.update(user);
        return user;
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<User> addToFriends(
            @PathVariable("id") int userId,
            @PathVariable int friendId) {
        log.info("Запрос: PUT users/{}/friends/{}", userId, friendId);
        userDbStorage.addFriendship(userId, friendId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<User> removeFromFriends(
            @PathVariable("id") int userId,
            @PathVariable int friendId) {
        log.info("Запрос: DELETE users/{}/friends/{}", userId, friendId);
        userDbStorage.removeFriendship(userId, friendId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/{id}/friends")
    public Optional<Object> findFriendsOfUser(@PathVariable(value = "id") int userId) {
        log.info("Запрос: GET /users/{}/friends", userId);
        return userDbStorage.getAllIdsFriendsByUserId(userId);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @PathVariable("id") int userId,
            @PathVariable int otherId) {
        log.info("Запрос: GET /users/{}/friends/common/{}", userId, otherId);
        return userDbStorage.getCommonFriendsList(userId, otherId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidIdException(final InvalidIdException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Нет такого id!", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInvalidParameterCounter(final InvalidParameterCounter e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Внутренняя ошибка сервера", e.getMessage());
    }
}