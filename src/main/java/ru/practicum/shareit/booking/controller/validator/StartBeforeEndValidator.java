package ru.practicum.shareit.booking.controller.validator;

import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingRequestDto> {
    @Override
    public boolean isValid(BookingRequestDto booking, ConstraintValidatorContext cxt) {
        return booking.getStart().isBefore(booking.getEnd());
    }
}
