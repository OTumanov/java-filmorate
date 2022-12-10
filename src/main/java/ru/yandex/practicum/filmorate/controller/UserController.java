package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.exception.InvalidParameterCounter;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.response.ErrorResponse;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Запрос: POST /users");
        userService.addUser(user);
        return user;
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable(value = "id") int userId) {
        log.info("Запрос: GET /users/{}", userId);
        return userService.getUser(userId);
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Запрос: GET /users");
        return userService.getAllUsers();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Запрос: PUT /users");
        userService.updateUser(user);
        return user;
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<User> addToFriends(
            @PathVariable("id") int userId,
            @PathVariable int friendId) {
        log.info("Запрос: PUT users/{}/friends/{}", userId, friendId);
        userService.addAFriend(userId, friendId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<User> removeFromFriends(
            @PathVariable("id") int userId,
            @PathVariable int friendId) {
        log.info("Запрос: DELETE users/{}/friends/{}", userId, friendId);
        userService.removeAFriend(userId, friendId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> findFriendsOfUser(@PathVariable(value = "id") int userId) {
        log.info("Запрос: GET /users/{}/friends", userId);
        return userService.getFriendsOfUser(userId);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @PathVariable("id") int userId,
            @PathVariable int otherId) {
        log.info("Запрос: GET /users/{}/friends/common/{}", userId, otherId);
        return userService.getCommonFriendsOfUser(userId, otherId);
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