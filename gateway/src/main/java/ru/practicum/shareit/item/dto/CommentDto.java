package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validateGroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentDto {
    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String text;
}
