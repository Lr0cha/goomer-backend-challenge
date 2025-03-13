package com.api.goomer.services;

import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.exceptions.EntityIsNotFoundException;
import com.api.goomer.exceptions.UniqueViolationException;
import com.api.goomer.repositories.RestaurantRepository;

import com.api.goomer.repositories.specifications.RestaurantSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    @Transactional(readOnly = true)
    public Page<Restaurant> findAll(String name, String address, Pageable pageable) {
        Specification<Restaurant> specification = RestaurantSpecification.withFilters(name, address);
        return repository.findAll(specification,pageable);
    }

    @Transactional(readOnly = true)
    public Restaurant findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityIsNotFoundException("Restaurante não encontrado"));
    }

    @Transactional
    public Restaurant create(Restaurant restaurant) {
        if(repository.findByNameAndAddress(restaurant.getName(), restaurant.getAddress()) != null){
            throw new UniqueViolationException(String.format("Restaurante: '%s' já existe neste endereço",restaurant.getName()));
        }
        return repository.save(restaurant);
    }

    @Transactional
    public void update(UUID id, Restaurant updatedRestaurant) {
        Restaurant restaurant = findById(id);
        restaurant.setUrlImage(updatedRestaurant.getUrlImage());
        restaurant.setAddress(updatedRestaurant.getAddress());
        restaurant.setName(updatedRestaurant.getName());
        restaurant.setOpeningHours(updatedRestaurant.getOpeningHours());
        repository.save(restaurant);
    }

    @Transactional
    public void delete(UUID id) {
        Restaurant restaurant = findById(id);
        repository.delete(restaurant);
    }
}
