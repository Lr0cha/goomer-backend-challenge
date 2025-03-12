package com.api.goomer.web.validations.promotionalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalTime;

public class TimeIntervalValidator implements ConstraintValidator<ValidTimeInterval, LocalTime[]> {

    @Override
    public boolean isValid(LocalTime[] value, ConstraintValidatorContext context) {
        if (value == null || value.length != 2) {
            return false;
        }

        LocalTime startTime = value[0];
        LocalTime endTime = value[1];

        if (startTime == null || endTime == null) {
            return false;
        }

        if (!startTime.isBefore(endTime)) {
            return false;
        }

        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes() >= 15;
    }
}