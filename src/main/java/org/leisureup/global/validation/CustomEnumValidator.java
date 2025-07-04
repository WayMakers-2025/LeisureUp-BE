package org.leisureup.global.validation;

import jakarta.validation.*;
import java.util.*;

/**
 * {@code enum} class validation 여부를 확인하는 class
 */
public class CustomEnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private ValidEnum validEnum;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        validEnum = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        Object[] enumValues = this.validEnum.target().getEnumConstants();

        return enumValues != null &&
               Arrays.asList(enumValues).contains(value);
    }
}
