package ru.practicum.shareit.user.controller.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UserIdExistValidator implements ConstraintValidator<UserIdExist, Long> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext cxt) {
        if (id == null) return false;
        if (userRepository.findById(id).isEmpty())
            throw new NoEntityException("User with id=" + id + " isn't found.");
        return true;
    }
}
