package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.validator.StartBeforeEnd;
import ru.practicum.shareit.validateGroup.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient client;


    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long bookerId,
                                                @Validated(Create.class) @RequestBody @StartBeforeEnd
                                                BookingDto bookingDto) {
        log.info("Try to create new booking. User: {}, Item : {}.", bookerId, bookingDto.getItemId());
        return client.create(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                                 @PathVariable @Positive Long bookingId,
                                                 @RequestParam("approved") boolean approved) {
        log.info("Try to approve booking. User: {}, Booking :{}, is approved?: {}.", userId, bookingId, approved);
        return client.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                             @PathVariable @Positive Long bookingId) {
        log.info("Try to getBooking. User: {}, Booking: {}.", userId, bookingId);
        return client.get(userId, bookingId);

    }

    @GetMapping
    public ResponseEntity<Object> getBookingByBooker(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Try to getBooking by booker and state. User: {}, State: {}. An return a {} bookings from {}.",
                userId, state, size, from);
        return client.getAllByBooker(userId, state, from, size);

    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Try to getBooking by owner of Items and state. User: {}, State: {}. An return a {} bookings from {}.",
                userId, state, size, from);
        return client.getAllByOwner(userId, state, from, size);

    }


}
