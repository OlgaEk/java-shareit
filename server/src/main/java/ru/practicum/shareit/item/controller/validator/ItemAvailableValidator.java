package ru.practicum.shareit.item.controller.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.exception.NotValidRequestException;
import ru.practicum.shareit.item.storage.ItemRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ItemAvailableValidator implements ConstraintValidator<ItemAvailable, Long> {
    private final ItemRepository itemRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext cxt) {
        if (id == null) return false;
        if (!itemRepository.findById(id).orElseThrow(() -> new NoEntityException("Item with id=" + id + " isn't found."))
                .isAvailable())
            throw new NotValidRequestException("Item with id=" + id + " isn't available for booking.");
        return true;
    }
}
