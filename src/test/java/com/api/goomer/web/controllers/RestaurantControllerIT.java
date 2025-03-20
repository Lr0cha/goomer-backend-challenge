package com.api.goomer.web.controllers;

import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.repositories.RestaurantRepository;
import com.api.goomer.services.RestaurantService;
import com.api.goomer.utils.CreateTestEntity;
import com.api.goomer.web.dtos.restaurant.RestaurantCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RestaurantControllerIT {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant1;
    private RestaurantCreateDto newRestaurant;
    private final UUID nonExistingRestaurantId = UUID.randomUUID();

    @BeforeEach
    void setup(){
        restaurant1 = CreateTestEntity.restaurant();
        restaurantService.create(restaurant1);

        newRestaurant = new RestaurantCreateDto("https://example.com/image2.jpg",
                "Restaurante B",
                "Rua b, 123, Bairro Y",
                "Segunda a Sabádo: 18:00 - 22:00");
    }

    @AfterEach
    void cleanUp(){
        restaurantRepository.deleteAll();
    }

    @Test
    @DisplayName("should show all restaurants successfully (200 - ok)")
    void findAll200() throws Exception {
        mockMvc.perform(get("/api/restaurants")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(restaurant1.getName())));
    }

    @Test
    @DisplayName("should show a restaurant by Id successfully (200 - ok)")
    void findRestaurantById200() throws Exception {
        mockMvc.perform(get("/api/restaurants/{id}", restaurant1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(restaurant1.getName())));
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when restaurant not found (404 - not found)")
    void findRestaurantById404() throws Exception {
        mockMvc.perform(get("/api/restaurants/{id}", nonExistingRestaurantId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should create a restaurant successfully when everything is ok (201 - created)")
    void createRestaurant201() throws Exception {
        mockMvc.perform(post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newRestaurant)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newRestaurant.name())));
    }

    @Test
    @DisplayName("should throw UniqueViolationException when the restaurant already exists in the address (409 - conflict)")
    void createRestaurant409() throws Exception {
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(restaurant1)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("should throw MethodArgumentNotValidException when data field is invalid (422 - unprocessable entity)")
    void createRestaurant422() throws Exception {
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new RestaurantCreateDto("","Restaurante",
                                "",""))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("should delete a restaurant by id successfully (204 - no content)")
    void updateRestaurant204() throws Exception {
        mockMvc.perform(put("/api/restaurants/{id}", restaurant1.getId() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newRestaurant)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when updating a non-existing restaurant (404 - not found)")
    void updateRestaurant404() throws Exception {
        mockMvc.perform(put("/api/restaurants/{id}", nonExistingRestaurantId )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newRestaurant)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should throw MethodArgumentNotValidException when data field is invalid (422 - unprocessable entity)")
    void updateRestaurant422() throws Exception {
        mockMvc.perform(put("/api/restaurants/{id}", restaurant1.getId() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new RestaurantCreateDto("","Restaurante",
                                "Rua",""))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("should delete restaurant by id successfully (204 - no content)")
    void deleteRestaurant204() throws Exception {
        mockMvc.perform(delete("/api/restaurants/{id}",restaurant1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when deleting a non-existing restaurant (404 - not found)")
    void deleteRestaurant404() throws Exception {
        mockMvc.perform(delete("/api/restaurants/{id}",nonExistingRestaurantId))
                .andExpect(status().isNotFound());
    }
}