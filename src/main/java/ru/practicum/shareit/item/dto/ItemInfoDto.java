package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * // This class describes the entity of Item (id and name), that returns to users
 */
@Data
public class ItemInfoDto {
    private Long id;
    private String name;
}
