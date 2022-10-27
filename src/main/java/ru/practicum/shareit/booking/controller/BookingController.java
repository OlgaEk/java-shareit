package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.controller.validator.BookingIdExist;
import ru.practicum.shareit.booking.controller.validator.StartBeforeEnd;
import ru.practicum.shareit.booking.validateGroup.Create;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.controller.validator.UserIdExist;

import java.util.List;

/**
 * // TODO .
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
            @RequestParam(value = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllByBooker(userId, state);

    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingByOwner(
            @RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllByOwner(userId, state);

    }


}
