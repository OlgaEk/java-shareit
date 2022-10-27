package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingBasicDataDto;

@Data
public class ItemInfoDto extends ItemDto {
    private BookingBasicDataDto lastBooking;
    private BookingBasicDataDto nextBooking;
}
