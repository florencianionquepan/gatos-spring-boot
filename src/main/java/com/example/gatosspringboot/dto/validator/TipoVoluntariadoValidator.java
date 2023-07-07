package com.example.gatosspringboot.dto.validator;

import com.example.gatosspringboot.model.TipoVoluntariado;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class TipoVoluntariadoValidator implements ConstraintValidator<FieldTipoVoluntariado, List<TipoVoluntariado>> {

    @Override
    public void initialize(FieldTipoVoluntariado constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<TipoVoluntariado> tipoVoluntariados, ConstraintValidatorContext constraintValidatorContext) {
        List<TipoVoluntariado> valoresPosibles = Arrays.asList(TipoVoluntariado.values());
        for (TipoVoluntariado tipoVoluntariado : tipoVoluntariados) {
            if (!valoresPosibles.contains(tipoVoluntariado)) {
                return false;
            }
        }
        return true;
    }
}
