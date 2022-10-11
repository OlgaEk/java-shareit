package ru.practicum.shareit.user.controller.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UserIdExistValidator implements ConstraintValidator<UserIdExist, Long> {
    private final UserStorage userStorage;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext cxt) {
        if (id == null) return false;
        if (!userStorage.containsUserId(id))
            throw new NoEntityException("User with id=" + id + " isn't found.");
        return true;
    }
}
