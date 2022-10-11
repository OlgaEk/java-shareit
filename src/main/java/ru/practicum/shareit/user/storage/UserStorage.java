package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    void delete(Long id);

    User update(User user);

    List<User> getAll();

    boolean containsUserEmail(String email);

    boolean containsUserId(Long id);

    User getUserById(Long id);

    Optional<User> getUserByEmail(String email);

}
