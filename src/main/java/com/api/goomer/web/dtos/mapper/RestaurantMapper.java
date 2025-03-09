package com.api.goomer.web.dtos.mapper;

import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.web.dtos.restaurant.RestaurantCreateDto;
import com.api.goomer.web.dtos.restaurant.RestaurantResponseDto;

import java.util.ArrayList;

public class RestaurantMapper {

    public static RestaurantResponseDto toDto(Restaurant restaurant){
        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getUrlImage(),
                restaurant.getName(),
                restaurant.getOpeningHours(),
                restaurant.getAddress()
        );
    }

    public static Restaurant toRestaurant(RestaurantCreateDto dto){
        return new Restaurant(
                null,
                dto.urlImage(),
                dto.name(),
                dto.address(),
                dto.openingHours(),
                new ArrayList<>()
        );
    }
}

