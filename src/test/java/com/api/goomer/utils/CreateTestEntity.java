package com.api.goomer.utils;

import com.api.goomer.entities.category.Category;
import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.web.dtos.product.ProductCreateDto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

public class CreateTestEntity {

    public static Restaurant restaurant(){
        return new Restaurant(null,
                "https://example.com/image1.jpg",
                "Restaurante A",
                "Rua a, 123, Bairro X",
                "Segunda a Sabádo: 19:00 - 23:00",
                new ArrayList<>());
    }

    public static Restaurant restaurant(UUID id){
        return new Restaurant(id,
                "https://example.com/image1.jpg",
                "Restaurante A",
                "Rua a, 123, Bairro X",
                "Segunda a Sabádo: 19:00 - 23:00",
                new ArrayList<>());
    }

    public static Category category(){
        return new Category("Categoria A");
    }

    public static Category category(Long id) {
        return new Category(id,"Categoria A");
    }

    public static ProductCreateDto product(Category category, Restaurant restaurant){
        LocalTime[] times = new LocalTime[]{LocalTime.of(9, 0), LocalTime.of(12, 30)};

        return new ProductCreateDto("https://example.com/image.jpg","Produto A", new BigDecimal("49.99"),
                1L,true,"Desconto especial de 10%",new BigDecimal("44.99"),
                "Segunda, Quarta, Sexta", times ,restaurant.getId());
    }
}
