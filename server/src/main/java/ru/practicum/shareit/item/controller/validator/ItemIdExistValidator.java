package ru.practicum.shareit.item.controller.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.item.storage.ItemRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ItemIdExistValidator implements ConstraintValidator<ItemIdExist, Long> {
    private final ItemRepository itemRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext cxt) {
        if (id == null) return false;
        if (itemRepository.findById(id).isEmpty())
            throw new NoEntityException("Item with id=" + id + " isn't found.");
        return true;
    }
}
