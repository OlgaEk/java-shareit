package ru.practicum.shareit.user.model.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.storage.memory.UserStorage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserStorage userStorage;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        if (email == null) return false;
        if (userStorage.containsUserEmail(email))
            throw new AlreadyExistException("Email " + email + " is already registered");
        return true;
    }

}
