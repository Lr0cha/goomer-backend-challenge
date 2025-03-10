package com.api.goomer.repositories;

import com.api.goomer.entities.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    Restaurant findByNameAndAddress(String name, String address);
}
