package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.validateGroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String text;
    private String authorName;
    private LocalDateTime created;
}
