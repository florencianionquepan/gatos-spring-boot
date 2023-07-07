package com.example.gatosspringboot.dto.validator;

import com.example.gatosspringboot.model.TipoVoluntariado;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class TipoVoluntariadoValidator implements ConstraintValidator<FieldTipoVoluntariado, TipoVoluntariado> {

    @Override
    public void initialize(FieldTipoVoluntariado constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TipoVoluntariado tipoVoluntariado, ConstraintValidatorContext constraintValidatorContext) {
        List<TipoVoluntariado> valoresPosibles = Arrays.asList(TipoVoluntariado.values());
        return valoresPosibles.contains(tipoVoluntariado);
    }
}
