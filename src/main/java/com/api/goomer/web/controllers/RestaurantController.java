package com.api.goomer.web.controllers;

import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.services.RestaurantService;
import com.api.goomer.web.dtos.mapper.RestaurantMapper;
import com.api.goomer.web.dtos.restaurant.RestaurantCreateDto;
import com.api.goomer.web.dtos.restaurant.RestaurantResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService service;

    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDto>> findAll(Pageable pageable){
       Page<Restaurant> restaurants =  service.findAll(pageable);
        return ResponseEntity.ok(restaurants.map(RestaurantMapper::toDto));
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Restaurant> findRestaurantById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantCreateDto restaurantCreateDto){
        Restaurant restaurantCreated = service.create(RestaurantMapper.toRestaurant(restaurantCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantMapper.toDto(restaurantCreated));
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable UUID id, @Valid @RequestBody RestaurantCreateDto dto){
        service.update(id,RestaurantMapper.toRestaurant(dto));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
