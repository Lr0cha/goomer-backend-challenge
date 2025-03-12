package com.api.goomer.web.validations.promotionalTime;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TimeIntervalValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME) //em tempo de execução
public @interface ValidTimeInterval {

    String message() default "O horário de término deve ser maior que o de início e ter uma diferença mínima de 15 minutos.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
