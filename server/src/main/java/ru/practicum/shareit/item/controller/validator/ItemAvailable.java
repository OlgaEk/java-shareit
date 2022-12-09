package ru.practicum.shareit.item.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ItemAvailableValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemAvailable {
    String message() default "id is null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
