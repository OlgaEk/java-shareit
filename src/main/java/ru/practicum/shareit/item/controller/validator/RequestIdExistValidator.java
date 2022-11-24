package ru.practicum.shareit.item.controller.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class RequestIdExistValidator implements ConstraintValidator<RequestIdExist, Long> {
    private final ItemRequestRepository requestRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext ctx) {
        if (id == null) return true;
        if (requestRepository.findById(id).isEmpty())
            throw new NoEntityException("Request with id=" + id + " isn't found.");
        return true;
    }
}
