package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.controller.validator.BookingIdExist;
import ru.practicum.shareit.booking.controller.validator.StartBeforeEnd;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.validateGroup.Create;
import ru.practicum.shareit.user.controller.validator.UserIdExist;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

//Заменила на описание класса

/**
 * The class handles the user's request in /bookings".
 */
@Validated
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long bookerId,
                                            @Validated(Create.class) @RequestBody @StartBeforeEnd
                                            BookingRequestDto bookingRequestDto) {
        return bookingService.create(bookerId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                             @PathVariable @BookingIdExist Long bookingId,
                                             @RequestParam("approved") boolean approved
    ) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                         @PathVariable @BookingIdExist Long bookingId) {
        return bookingService.get(userId, bookingId);

    }

    @GetMapping
    public List<BookingResponseDto> getBookingByBooker(
            @RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return bookingService.getAllByBooker(userId, state, pageRequest);

    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingByOwner(
            @RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return bookingService.getAllByOwner(userId, state, pageRequest);

    }


}
