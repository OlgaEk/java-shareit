package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validateGroup.Create;
import ru.practicum.shareit.validateGroup.Update;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient client;


    @PostMapping
    public ResponseEntity<Object> createUser(@Validated(Create.class) @RequestBody UserDto user) {
        log.info("Try to create user. User name:{}", user.getName());
        return client.create(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Try to delete user. User:{}", userId);
        client.delete(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @Validated(Update.class) @RequestBody UserDto user) {
        log.info("Try to update user. User:{}", userId);
        return client.update(userId, user);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Try to get all users");
        return client.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("Try to get user by id. User: {}", userId);
        return client.getUser(userId);
    }
}
