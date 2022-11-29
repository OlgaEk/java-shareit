package ru.practicum.shareit.booking.dto;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class BookingMapperTest {

    @InjectMocks
    BookingMapperImpl mapper;

    BookingRequestDto bookingRequestDto;
    Booking booking;
    User user;
    Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        item = new Item();
        item.setId(1L);

        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setId(1L);

        booking = new Booking();
        booking.setId(1L);



    }

    @Test
    void verifyRequestDtoToBooking() {
        Booking result = mapper.requestDtoToBooking(bookingRequestDto);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(bookingRequestDto.getId(), result.getId())
        );

        assertNull(mapper.requestDtoToBooking(null));
    }

    @Test
    void verifyBookingToResponseDto() {
        BookingResponseDto result = mapper.bookingToResponseDto(booking);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(booking.getId(), result.getId())
        );

        booking.setItem(item);
        BookingResponseDto resultWithItem = mapper.bookingToResponseDto(booking);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(booking.getId(), resultWithItem.getId()),
                () -> assertEquals(booking.getItem().getId(), resultWithItem.getItem().getId())
        );


        assertNull(mapper.bookingToResponseDto(null));
    }

    @Test
    void verifyBookingToBasicDto() {
        BookingBasicDataDto result = mapper.bookingToBasicDto(booking);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(booking.getId(), result.getId())
        );
        booking.setBooker(user);
        BookingBasicDataDto resultWithUser = mapper.bookingToBasicDto(booking);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(booking.getId(), resultWithUser.getId()),
                () -> assertEquals(booking.getBooker().getId(), resultWithUser.getBookerId())
        );

        assertNull(mapper.bookingToBasicDto(null));
    }

    @Test
    void verifyBookingsToResponse() {
        List<BookingResponseDto> result = mapper.bookingsToResponse(List.of(booking));
        assertNotNull(result);
        assertAll(
                () -> assertEquals(booking.getId(), result.get(0).getId())
        );

        assertNull(mapper.bookingsToResponse(null));
    }


}
