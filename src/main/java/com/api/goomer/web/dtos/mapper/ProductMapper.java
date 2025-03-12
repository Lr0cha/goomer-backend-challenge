package com.api.goomer.web.dtos.mapper;

import com.api.goomer.entities.category.Category;
import com.api.goomer.entities.product.Product;
import com.api.goomer.entities.product.embedded.Offer;
import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.web.dtos.product.ProductCreateDto;
import com.api.goomer.web.dtos.product.ProductResponseDto;


public class ProductMapper {

    public static ProductResponseDto toDto(Product product){
        Offer offer = product.getOffer();

        return new ProductResponseDto(
                product.getId(),
                product.getUrlImage(),
                product.getProductName(),
                product.getPrice(),
                product.getCategory(),
                product.getIsOnOffer(),
                offer != null ? offer.getPromotionalDescription() : null,
                offer != null ? offer.getPromotionalPrice() : null,
                offer != null ? offer.getPromotionalDays() : null,
                offer != null ? offer.getPromotionalStartTime() : null,
                offer != null ? offer.getPromotionalEndTime() : null,
                product.getRestaurant()
                );
    }

    public static Product toProduct(ProductCreateDto dto,
                                    Restaurant restaurant, Category category){
        Offer offer;
        if(dto.isOnOffer()){
            offer = new Offer(
                dto.promotionalDescription(),
                dto.promotionalPrice(),
                dto.promotionalDays(),
                dto.promotionalTime()[0],
                dto.promotionalTime()[1]
                    );
        }else{
             offer = new Offer();
        }

        return new Product(
                null,
                dto.urlImage(),
                dto.productName(),
                dto.price(),
                category,
                dto.isOnOffer(),
                offer,
                restaurant
        );
    }
}
