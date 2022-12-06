package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.controller.validator.RequestIdExist;
import ru.practicum.shareit.item.validateGroup.Create;

/**
 * // This class describes the entity of Item (basic data and request id), that returns to users
 */

@Data
public class ItemRequestInfoDto extends ItemBaseDto {
    @RequestIdExist(groups = Create.class)
    private Long requestId;
}
