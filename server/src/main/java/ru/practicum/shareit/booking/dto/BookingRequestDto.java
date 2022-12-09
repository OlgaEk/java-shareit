package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.validateGroup.Create;
import ru.practicum.shareit.item.controller.validator.ItemAvailable;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
public class BookingRequestDto {
    private Long id;
    @Future(message = "The date of booking must be in future", groups = Create.class)
    @NotNull(message = "The date of booking must be not empty", groups = Create.class)
    private LocalDateTime start;
    @Future(message = "The date of booking must be in future", groups = Create.class)
    @NotNull(message = "The date of booking must be not empty", groups = Create.class)
    private LocalDateTime end;
    @ItemAvailable(groups = Create.class)
    private Long itemId;
}
