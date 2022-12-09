package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.validateGroup.Create;
import ru.practicum.shareit.user.validateGroup.Update;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * //  This class describes the entity of User
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The name must be not empty", groups = Create.class)
    private String name;

    @NotNull(message = "The email must be not empty", groups = Create.class)
    @NotBlank(message = "The email must be not empty", groups = Create.class)
    @Email(message = "Email address not valid", groups = {Create.class, Update.class})
    @Column(unique = true, nullable = false)
    private String email;
}

