package ru.practicum.shareit.booking.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingBasicDataDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Mapper
public interface BookingMapper {
    Booking requestDtoToBooking(BookingRequestDto bookingRequestDto);

    BookingResponseDto bookingToResponseDto(Booking booking);

    @Mapping(target = "bookerId", source = "booker.id")
    BookingBasicDataDto bookingToBasicDto(Booking booking);

    List<BookingResponseDto> bookingsToResponse(List<Booking> bookings);
}
