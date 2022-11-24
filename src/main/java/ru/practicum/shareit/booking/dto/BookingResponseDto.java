package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserDto booker;
    private ItemInfoDto item;
    private BookingStatus status;
}
