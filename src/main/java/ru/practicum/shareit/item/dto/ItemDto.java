package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.validateGroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * // This class describes the entity of Item, that returns to users
 */
@Data
public class ItemDto {

    private Long id;

    @NotBlank(message = "The name of item must be not empty", groups = Create.class)
    private String name;

    @NotNull(message = "The description of item is required", groups = Create.class)
    @NotBlank(message = "The description of item is required", groups = Create.class)
    private String description;

    @NotNull(message = "The item must have a status", groups = Create.class)
    private Boolean available;

    private List<CommentDto> comments;
}
