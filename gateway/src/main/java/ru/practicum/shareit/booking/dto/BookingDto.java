package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.validateGroup.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    @Future(message = "The date of booking must be in future", groups = Create.class)
    @NotNull(message = "The date of booking must be not empty", groups = Create.class)
    private LocalDateTime start;
    @Future(message = "The date of booking must be in future", groups = Create.class)
    @NotNull(message = "The date of booking must be not empty", groups = Create.class)
    private LocalDateTime end;
    @Positive(message = "Item id must be not null and positive", groups = Create.class)
    private Long itemId;
}
