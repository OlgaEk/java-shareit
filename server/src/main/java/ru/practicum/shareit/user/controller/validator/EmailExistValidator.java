package ru.practicum.shareit.user.controller.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailExistValidator implements ConstraintValidator<EmailExist, User> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(User user, ConstraintValidatorContext cxt) {
        if (user.getEmail().isEmpty()) return true;
        if (!userRepository.findByEmail(user.getEmail()).isEmpty())
            throw new AlreadyExistException("Email " + user.getEmail() + " is already exist");
        return true;
    }
}
