package com.api.goomer.web.dtos.product;

import com.api.goomer.entities.category.Category;
import com.api.goomer.entities.restaurant.Restaurant;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String urlImage,
        String productName,
        BigDecimal price,
        Category category,
        Boolean isOnOffer,
        String promotionalDescription,
        BigDecimal promotionalPrice,
        String promotionalDays,
        LocalTime promotionalStartTime,
        LocalTime promotionalEndTime,
        Restaurant restaurant
) {
}
