package com.github.stoynko.easydoc.user.validation.validator;

import com.github.stoynko.easydoc.user.validation.annotation.ValidBirthDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    private static final LocalDate MIN_DATE = LocalDate.of(1930, 1, 1);

    @Override
    public void initialize(ValidBirthDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return true;
        }

        LocalDate today = LocalDate.now();
        LocalDate soonestAllowed = today.minusYears(18);

        if (date.isBefore(MIN_DATE)) {
            return false;
        }

        if (date.isAfter(soonestAllowed)) {
            return false;
        }

        return true;
    }
}
