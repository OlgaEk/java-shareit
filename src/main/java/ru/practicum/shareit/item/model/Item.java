package ru.practicum.shareit.item.model;

import lombok.Data;

/**
 * // This class describes the entity of Item, that save in base
 */
@Data
public class Item {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private Long ownerId;
}
