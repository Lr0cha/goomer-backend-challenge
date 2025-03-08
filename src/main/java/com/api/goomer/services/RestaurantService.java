package com.api.goomer.services;

import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.repositories.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    @Transactional(readOnly = true)
    public Page<Restaurant> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Restaurant findById(UUID id) { //404
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public Restaurant create(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    @Transactional
    public void update(UUID id, Restaurant newRestaurant) {
        Restaurant restaurant = findById(id);
        restaurant.setAddress(newRestaurant.getAddress());
        restaurant.setName(newRestaurant.getName());
        // ...
        repository.save(restaurant);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
