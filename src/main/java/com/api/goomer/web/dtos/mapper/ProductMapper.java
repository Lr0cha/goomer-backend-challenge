package com.api.goomer.web.dtos.mapper;

import com.api.goomer.entities.category.Category;
import com.api.goomer.entities.product.Product;
import com.api.goomer.entities.product.embedded.Offer;
import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.web.dtos.product.ProductCreateDto;
import com.api.goomer.web.dtos.product.ProductResponseDto;

import java.util.UUID;

public class ProductMapper {

    public static ProductResponseDto toDto(Product product){
        return new ProductResponseDto(
                product.getId(),
                product.getUrlImage(),
                product.getProductName(),
                product.getPrice(),
                product.getCategory(),
                product.getIsOnOffer(),
                product.getOffer().getPromotionalDescription(),
                product.getOffer().getPromotionalPrice(),
                product.getOffer().getPromotionalDays(),
                product.getOffer().getPromotionalStartTime(),
                product.getOffer().getPromotionalEndTime(),
                product.getRestaurant()
                );
    }

    public static Product toProduct(ProductCreateDto dto,
                                    Restaurant restaurant, Category category){
        Offer offer = new Offer(
                dto.promotionalDescription(),
                dto.promotionalPrice(),
                dto.promotionalDays(),
                dto.promotionalStartTime(),
                dto.promotionalEndTime());

        return new Product(
                null,
                dto.urlImage(),
                dto.productName(),
                dto.price(),
                category,
                false,
                offer,
                restaurant
        );
    }
}
