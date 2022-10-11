package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
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
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@Validated(Create.class) @RequestBody User user) {
        return userService.create(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @UserIdExist Long userId) {
        userService.delete(userId);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable @UserIdExist Long userId,
                           @Validated(Update.class) @RequestBody User user) {
        return userService.update(userId, user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable @UserIdExist Long userId) {
        return userService.getUser(userId);
    }

}
