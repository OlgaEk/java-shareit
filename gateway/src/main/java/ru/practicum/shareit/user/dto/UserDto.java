package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.validateGroup.Create;
import ru.practicum.shareit.validateGroup.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    @NotBlank(message = "The name must be not empty", groups = Create.class)
    private String name;

    @NotNull(message = "The email must be not empty", groups = Create.class)
    @NotBlank(message = "The email must be not empty", groups = Create.class)
    @Email(message = "Email address not valid", groups = {Create.class, Update.class})
    private String email;
}
