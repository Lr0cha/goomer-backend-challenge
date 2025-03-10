package com.api.goomer.web.dtos.restaurant;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record RestaurantCreateDto(
        @NotBlank @URL
        String urlImage,
        @NotBlank @Size(min=5, max=100)
        String name,
        @NotBlank
        String address,
        @NotBlank
        String openingHours
) {
}
