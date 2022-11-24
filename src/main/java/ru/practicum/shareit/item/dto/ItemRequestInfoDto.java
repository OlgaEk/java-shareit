package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * // This class describes the entity of Item (basic data and request id), that returns to users
 */

@Data
public class ItemRequestInfoDto extends ItemBaseDto {
    private Long requestId;
}
