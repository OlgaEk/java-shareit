package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.validateGroup.Create;
import ru.practicum.shareit.item.controller.validator.ItemAvailable;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
public class BookingRequestDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;
    @ItemAvailable(groups = Create.class)
    private Long itemId;
}
