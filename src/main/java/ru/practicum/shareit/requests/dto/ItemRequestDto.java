package ru.practicum.shareit.requests.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemRequestInfoDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * // /**
 * * // This class describes the entity of request, that returns to users
 */

@Data
public class ItemRequestDto {

    private Long id;
    @NotNull(message = "The description of request for item is required")
    @NotBlank(message = "The description of request for item is required")
    private String description;
    private LocalDateTime created;
    private List<ItemRequestInfoDto> items;

}
