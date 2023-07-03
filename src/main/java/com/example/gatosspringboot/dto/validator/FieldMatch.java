package com.example.gatosspringboot.dto.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
public @interface FieldMatch {
    String message() default "Los campos no coinciden";
    String first();
    String second();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
