package com.api.goomer.web.validations.offer;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OfferFieldsValidator.class)
public @interface OfferFieldsRequiredIfOnOffer {
    String message() default "Os campos promocionais são obrigatórios quando 'isOnOffer' é true.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
