package ru.practicum.shareit.user.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailExist {
    String message() default "email is null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

