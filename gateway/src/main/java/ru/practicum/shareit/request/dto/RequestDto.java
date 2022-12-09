package ru.practicum.shareit.request.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestDto {
    @NotNull(message = "The description of request for item is required")
    @NotBlank(message = "The description of request for item is required")
    private String description;
}
