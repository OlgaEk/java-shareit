package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * // This class describes the entity of Item (basic data), that returns to users
 */
@Data
public class ItemBaseDto {
    private Long id;


    private String name;


    private String description;


    private Boolean available;

}
