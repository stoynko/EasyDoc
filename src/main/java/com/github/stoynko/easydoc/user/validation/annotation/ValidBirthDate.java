package com.github.stoynko.easydoc.user.validation.annotation;

import com.github.stoynko.easydoc.user.validation.validator.BirthDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = BirthDateValidator.class)
@Documented
public @interface ValidBirthDate {

    String message() default "Please enter a valid date of birth";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
