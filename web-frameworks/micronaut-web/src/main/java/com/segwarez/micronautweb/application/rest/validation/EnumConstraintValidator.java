package com.segwarez.micronautweb.application.rest.validation;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;

@Singleton
public class EnumConstraintValidator implements ConstraintValidator<EnumValidator, String> {

    @Override
    public boolean isValid(@Nullable String valueForValidation,
                           @NonNull AnnotationValue<EnumValidator> annotationMetadata,
                           @NonNull ConstraintValidatorContext context) {
        var enumClass = annotationMetadata.get("enumClass", Class.class);
        if (enumClass.isPresent()) {
            for (Object enumValue : enumClass.get().getEnumConstants()) {
                if (valueForValidation.equals(enumValue.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}