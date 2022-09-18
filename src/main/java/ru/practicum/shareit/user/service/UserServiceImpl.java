package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public void delete(Long id) {
        userStorage.delete(id);
    }

    public User update(Long id, User user) {
        user.setId(id);
        emailValidation(user);
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUser(Long id) {
        return userStorage.getUserById(id);
    }

    private void emailValidation(User user) {
        if (user.getEmail() == null) return;
        Optional<User> userFromBase = userStorage.getUserByEmail(user.getEmail());
        if (userFromBase.isEmpty()) return;
        if (Objects.equals(userFromBase.get().getId(), user.getId())) return;
        throw new AlreadyExistException("Email " + user.getEmail() + " is already exist");
    }

    private void setMissingData(User userTo, User userFrom) {
        if (userTo.getName() == null || userTo.getName().isBlank())
            userTo.setName(userFrom.getName());
        if (userTo.getEmail() == null || userTo.getEmail().isBlank())
            userTo.setEmail(userFrom.getEmail());
    }
}
