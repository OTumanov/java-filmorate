package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.utils.MakeId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final MakeId id = new MakeId();
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        users.forEach((k, v) -> allUsers.add(v));
        return allUsers;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {

        user.setId(id.gen());
        users.put(user.getId(), user);
        log.info("Запрос POST /users " + user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {

        if (!users.containsKey(user.getId())) {
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Запрос PUT /users " + user);
        users.put(user.getId(), user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
