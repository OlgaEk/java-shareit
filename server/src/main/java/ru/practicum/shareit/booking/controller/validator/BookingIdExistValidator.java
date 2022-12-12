package ru.practicum.shareit.booking.controller.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NoEntityException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class BookingIdExistValidator implements ConstraintValidator<BookingIdExist, Long> {
    private final BookingRepository bookingRepository;

    @Override
    public boolean isValid(Long bookingId, ConstraintValidatorContext cxt) {
        if (bookingId == null) return false;
        if (bookingRepository.findById(bookingId).isEmpty())
            throw new NoEntityException("Booking with id=" + bookingId + " isn't found.");
        return true;
    }
}
