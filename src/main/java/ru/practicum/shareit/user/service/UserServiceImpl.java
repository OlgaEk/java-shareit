package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistException("Email " + user.getEmail() + " is already exist");
        }
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User update(Long id, User user) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new NoEntityException("User not found"));
        setNewData(user, userToUpdate);
        try {
            return userRepository.saveAndFlush(userToUpdate);
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistException("Email " + userToUpdate.getEmail() + " is already exist");
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoEntityException("User not found"));
    }

    private void setNewData(User user, User userToUpdate) {
        if (user.getName() != null) {
            if (!user.getName().isBlank())
                userToUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().isBlank())
                userToUpdate.setEmail(user.getEmail());
        }
    }
}
