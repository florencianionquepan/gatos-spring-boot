package com.example.gatosspringboot.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum,List<String>> {
    private List<String> valoresAceptados;
    //private Logger logger= LoggerFactory.getLogger(ValueOfEnumValidator.class);

    @Override
    public void initialize(ValueOfEnum annotation) {
        valoresAceptados= Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        if(values.isEmpty()){
            return false;
        }
        for(String value:values){
            if(!valoresAceptados.contains(value)){
                return false;
            }
        }
        return true;
    }
}
