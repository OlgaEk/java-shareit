package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class UserStorageInMemory implements UserStorage {
    private final HashMap<Long, User> userBase = new HashMap<>();
    private Long lastAssignedId = 0L;

    public User create(User user) {
        user.setId(++lastAssignedId);
        userBase.put(user.getId(), user);
        return user;
    }

    public void delete(Long id) {
        userBase.remove(id);
    }

    public List<User> getAll() {
        return new ArrayList<>(userBase.values());
    }

    public User getUserById(Long id) {
        return userBase.get(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userBase.values().stream().filter(s -> s.getEmail().equals(email)).findFirst();
    }

    public User update(User user) {
        User userToUpdate = userBase.get(user.getId());
        if (user.getName() != null) {
            if (!user.getName().isBlank())
                userToUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().isBlank())
                userToUpdate.setEmail(user.getEmail());
        }
        userBase.put(user.getId(), userToUpdate);
        return userToUpdate;
    }

    public boolean containsUserEmail(String email) {
        return userBase.values().stream().anyMatch(s -> s.getEmail().equals(email));
    }

    public boolean containsUserId(Long id) {
        return userBase.containsKey(id);
    }

}
