package com.xm.crypto.recommendation.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Interface for validation whether crypto symbol is or is not supported
 */
@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = SupportedCryptoValidator.class)
@Documented
public @interface SupportedCrypto {
    String message() default "Not supported crypto";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}