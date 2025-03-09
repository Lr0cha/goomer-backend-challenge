package com.api.goomer.web.dtos.restaurant;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record RestaurantCreateDto(
        @NotBlank
        String urlImage,
        @NotBlank
        String name,
        @NotBlank
        String address,
        @NotBlank
        String openingHours
) {
}
