package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.controller.validator.UserIdExist;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.validateGroup.Create;
import ru.practicum.shareit.user.validateGroup.Update;

import java.util.List;

/**
 * // The class handles the user's request in "/users".
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@Validated(Create.class) @RequestBody User user) {
        log.info("Try to create user. User name:{}", user.getName());
        return userService.create(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @UserIdExist Long userId) {
        log.info("Try to delete user. User:{}", userId);
        userService.delete(userId);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable @UserIdExist Long userId,
                           @Validated(Update.class) @RequestBody User user) {
        log.info("Try to update user. User:{}", userId);
        return userService.update(userId, user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Try to get all users");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable @UserIdExist Long userId) {
        log.info("Try to get user by id. User: {}", userId);
        return userService.getUser(userId);
    }

}
