package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    void delete(Long id);

    User update(Long id, User user);

    List<User> getAll();

    User getUser(Long id);
}
