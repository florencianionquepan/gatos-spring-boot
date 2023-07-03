package com.example.gatosspringboot.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        Object firstFieldValue = beanWrapper.getPropertyValue(firstFieldName);
        Object secondFieldValue = beanWrapper.getPropertyValue(secondFieldName);

        // Verificar si los valores son iguales
        boolean isValid = Objects.equals(firstFieldValue, secondFieldValue);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(secondFieldName)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
