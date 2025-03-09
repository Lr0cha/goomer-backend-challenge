package com.api.goomer.web.dtos.product;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record ProductCreateDto(
        @NotBlank @URL
        String urlImage,
        @NotBlank @Size(min=3,max=100)
        String productName,
        @NotNull @Positive
        BigDecimal price,
        @NotNull @Min(1)
        Long categoryId,
        String promotionalDescription,
        BigDecimal promotionalPrice,
        String promotionalDays,
        LocalTime promotionalStartTime,
        LocalTime promotionalEndTime,
        @NotNull
        UUID restaurantId
) {

}
