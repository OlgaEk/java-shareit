package ru.practicum.shareit.item.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RequestIdExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestIdExist {
    String message() default "Request id isn't found.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
