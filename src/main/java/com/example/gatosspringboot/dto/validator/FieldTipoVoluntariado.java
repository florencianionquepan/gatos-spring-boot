package com.example.gatosspringboot.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TipoVoluntariadoValidator.class)
@Documented
public @interface FieldTipoVoluntariado {
    String message() default "TipoVoluntariado inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
