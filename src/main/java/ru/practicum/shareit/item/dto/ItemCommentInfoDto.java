package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.controller.validator.RequestIdExist;
import ru.practicum.shareit.item.validateGroup.Create;

import java.util.List;

/**
 * // This class describes the entity of Item (basic data and comments), that returns to users
 */
@Data
public class ItemCommentInfoDto extends ItemBaseDto {

    private List<CommentDto> comments;
    @RequestIdExist(groups = Create.class)
    private Long requestId;
}
