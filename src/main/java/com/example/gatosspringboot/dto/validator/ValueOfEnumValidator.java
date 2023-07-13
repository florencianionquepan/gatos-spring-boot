package com.example.gatosspringboot.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum,String> {
    private List<String> valoresAceptados;
    //private Logger logger= LoggerFactory.getLogger(ValueOfEnumValidator.class);

    @Override
    public void initialize(ValueOfEnum annotation) {
        valoresAceptados= Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }
        return valoresAceptados.contains(value);
    }
}
