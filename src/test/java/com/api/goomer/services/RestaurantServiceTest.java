package com.api.goomer.services;

import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.exceptions.EntityIsNotFoundException;
import com.api.goomer.exceptions.UniqueViolationException;
import com.api.goomer.repositories.RestaurantRepository;
import com.api.goomer.utils.CreateTestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository repository;

    @Autowired
    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private Restaurant updatedRestaurant;
    private UUID restaurantId;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        restaurant = CreateTestEntity.restaurant(UUID.randomUUID());
        restaurantId = restaurant.getId();

        updatedRestaurant = new Restaurant(null,"https://example.com/image2.jpg","Restaurante B",
                "Rua B  , 123, Bairro Y", "Segunda a Quarta: 10:00 - 22:00", new ArrayList<>());

    }


    @Test
    @DisplayName("should show a restaurant successfully by id when everything is ok")
    void findById200() {
        when(repository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        Restaurant result = restaurantService.findById(restaurantId);

        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Restaurante A", result.getName());
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when restaurant not found")
    void findById404() {
        when(repository.findById(restaurantId)).thenReturn(Optional.empty());

        EntityIsNotFoundException exception = assertThrows(
                EntityIsNotFoundException.class,
                () -> restaurantService.findById(restaurantId)
        );
        assertEquals("Restaurante não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("should create a restaurant successfully when everything is ok")
    void create201() {
        when(repository.findByNameAndAddress(anyString(), anyString())).thenReturn(null);

        when(repository.save(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant result = restaurantService.create(restaurant);
        assertNotNull(result);
        assertEquals("Restaurante A", result.getName());
        verify(repository, times(1)).save(restaurant);
    }

    @Test
    @DisplayName("should throw UniqueViolationException when the restaurant already exists in the address")
    void create409() {
        when(repository.findByNameAndAddress(anyString(), anyString())).thenReturn(new Restaurant());
        UniqueViolationException exception = assertThrows(
                UniqueViolationException.class,
                () -> restaurantService.create(restaurant)
        );
        assertEquals("Restaurante: 'Restaurante A' já existe neste endereço", exception.getMessage());
    }

    @Test
    @DisplayName("should delete a restaurant successfully")
    void delete204() {
        when(repository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        restaurantService.delete(restaurantId);

        verify(repository, times(1)).delete(restaurant);
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when deleting a non-existing restaurant")
    void delete404() {
        when(repository.findById(restaurantId)).thenReturn(Optional.empty());

        EntityIsNotFoundException exception = assertThrows(
                EntityIsNotFoundException.class,
                () -> restaurantService.update(restaurantId, updatedRestaurant)
        );
        assertEquals("Restaurante não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("should update a restaurant successfully when everything is ok")
    void update204() {
        when(repository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        restaurantService.update(restaurantId, updatedRestaurant);

        assertEquals("https://example.com/image2.jpg", restaurant.getUrlImage());
        assertEquals("Restaurante B", restaurant.getName());
        assertEquals("Rua B  , 123, Bairro Y", restaurant.getAddress());
        assertEquals("Segunda a Quarta: 10:00 - 22:00", restaurant.getOpeningHours());

        verify(repository, times(1)).save(restaurant);
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when the restaurant not found")
    void update404() {
        when(repository.findById(restaurantId)).thenReturn(Optional.empty());

        EntityIsNotFoundException exception = assertThrows(
                EntityIsNotFoundException.class,
                () -> restaurantService.update(restaurantId, updatedRestaurant)
        );
        assertEquals("Restaurante não encontrado", exception.getMessage());
    }
}