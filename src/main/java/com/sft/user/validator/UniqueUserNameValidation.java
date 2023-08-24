package com.sft.user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUserNameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserNameValidation {
    String message() default "user name must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
