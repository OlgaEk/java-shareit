package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.user.model.validator.UniqueEmail;
import ru.practicum.shareit.user.validateGroup.Create;
import ru.practicum.shareit.user.validateGroup.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * //  This class describes the entity of User
 */
@Data
public class User {
    private Long id;

    @NotBlank(message = "The name must be not empty", groups = Create.class)
    private String name;

    @NotNull(message = "The email must be not empty", groups = Create.class)
    @NotBlank(message = "The email must be not empty", groups = Create.class)
    @UniqueEmail(groups = Create.class)
    @Email(message = "Email address not valid", groups = {Create.class, Update.class})
    private String email;
}
