package com.api.goomer.repositories;

import com.api.goomer.entities.product.Product;
import com.api.goomer.entities.restaurant.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByRestaurant(Restaurant restaurant, Pageable pageable);
    Product findByProductNameAndRestaurant(String productName ,Restaurant restaurant);
}
