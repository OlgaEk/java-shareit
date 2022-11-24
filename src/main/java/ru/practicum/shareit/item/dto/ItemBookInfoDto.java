package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingBasicDataDto;

/**
 * // This class describes the entity of Item (basic and booking data), that returns to users
 */

@Data
public class ItemBookInfoDto extends ItemCommentInfoDto {
    private BookingBasicDataDto lastBooking;
    private BookingBasicDataDto nextBooking;
}
