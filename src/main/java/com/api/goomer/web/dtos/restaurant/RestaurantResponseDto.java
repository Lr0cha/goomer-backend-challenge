package com.api.goomer.web.dtos.restaurant;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RestaurantResponseDto(
        UUID id,
        String urlImage,
        String name,
        String address,
        String openingHours
) {

}
