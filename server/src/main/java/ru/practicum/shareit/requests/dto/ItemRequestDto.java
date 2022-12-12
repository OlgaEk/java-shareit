package ru.practicum.shareit.requests.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemRequestInfoDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * // /**
 * * // This class describes the entity of request, that returns to users
 */

@Data
public class ItemRequestDto {

    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemRequestInfoDto> items;

}
