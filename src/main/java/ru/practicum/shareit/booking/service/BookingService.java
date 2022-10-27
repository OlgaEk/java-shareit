package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingBasicDataDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto create(Long bookerId, BookingRequestDto bookingRequestDto);

    BookingResponseDto approve(Long userId, Long bookingId, boolean approved);

    BookingResponseDto get(Long userId, Long bookingId);

    List<BookingResponseDto> getAllByBooker(Long userId, String state);

    List<BookingResponseDto> getAllByOwner(Long userId, String state);

    BookingBasicDataDto getLastBooking(Long itemId);

    BookingBasicDataDto getNextBooking(Long itemId);
}
