package com.api.goomer.web.controllers;

import com.api.goomer.entities.category.Category;
import com.api.goomer.entities.product.Product;
import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.repositories.CategoryRepository;
import com.api.goomer.repositories.ProductRepository;
import com.api.goomer.repositories.RestaurantRepository;
import com.api.goomer.services.CategoryService;
import com.api.goomer.services.ProductService;
import com.api.goomer.services.RestaurantService;
import com.api.goomer.utils.CreateTestEntity;
import com.api.goomer.web.dtos.mapper.ProductMapper;
import com.api.goomer.web.dtos.product.ProductCreateDto;
import com.api.goomer.web.dtos.product.ProductResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerIT {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ProductRepository repository;

    private Restaurant restaurant;
    private ProductCreateDto productDto;
    private ProductCreateDto newProductDto;
    private Page<Product> productStored;
    private Category category;
    private final UUID nonExistingProductId = UUID.randomUUID();
    private final UUID NonExistingRestaurantId = UUID.randomUUID();
    private LocalTime[] times;

    @BeforeAll
    void createRestaurantAndCategory(){
        category = CreateTestEntity.category();
        categoryService.create(category);

        restaurant = CreateTestEntity.restaurant();
        restaurantService.create(restaurant);
    }

    @BeforeEach
    void setup(){
         times = new LocalTime[]{LocalTime.of(9, 0), LocalTime.of(12, 30)};


        productDto = CreateTestEntity.product(category,restaurant);
        productService.create(productDto);

        newProductDto = new ProductCreateDto("https://example.com/image2.jpg","Produto B", new BigDecimal("99.99"),
                category.getId(), true,"Desconto especial de 0%",new BigDecimal("99.99"),
                "Segunda, Quarta, Sexta", times ,restaurant.getId());

        productStored = productService.findProductsByRestaurant(productDto.productName(),
                category.getDescription(), restaurant.getId(),Pageable.ofSize(1));
    }

    @AfterEach
    void cleanUp(){
        repository.deleteAll();
    }

    @Test
    @DisplayName("should show a product from a restaurant when everything is ok (200 - ok)")
    void getAllProductsByRestaurant200() throws Exception {
        mockMvc.perform(get("/api/products/{id}", restaurant.getId())
                        .param("name", "")
                        .param("category", "Categoria A")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].productName", is(productDto.productName())));
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when restaurant not found (404 - not found)")
    void getAllProductsByRestaurant404() throws Exception {
        mockMvc.perform(get("/api/products/{id}", NonExistingRestaurantId)
                        .param("name", "")
                        .param("category", "Categoria A")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should create a product successfully when everything is ok (201 - created)")
    void createProduct201() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newProductDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName", is("Produto B")));
    }

    @Test
    @DisplayName("should throw UniqueViolationException when the product already exists in the restaurant (409 - conflict)")
    void createProduct409() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("should throw MethodArgumentNotValidException when data field is invalid (422 - unprocessable entity)")
    void createProduct422() throws Exception{
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ProductCreateDto("","Produto B", new BigDecimal("99.99"),
                                1L,true,"Desconto especial de 0%",new BigDecimal("99.99"),
                                "Segunda, Quarta, Sexta", times ,restaurant.getId()))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("should update a product by id successfully when everything is ok (204 - no content)")
    void updateProduct204() throws Exception {
        mockMvc.perform(put("/api/products/{id}", productStored.getContent().getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newProductDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should throw UniqueViolationException when the product not found (404 - not found)")
    void updateProduct404() throws Exception {
        mockMvc.perform(put("/api/products/{id}", nonExistingProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newProductDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should throw MethodArgumentNotValidException when data field is invalid (422 - unprocessable entity)")
    void updateProduct422() throws Exception {
        mockMvc.perform(put("/api/products/{id}", productStored.getContent().getFirst().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new ProductCreateDto("","Produto B", new BigDecimal("99.99"),
                        1L,true,"Desconto especial de 0%",new BigDecimal("99.99"),
                        "Segunda, Quarta, Sexta", times ,restaurant.getId()))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("should delete a product by id sucessfully (204 - no content)")
    void deleteProduct204() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", productStored.getContent().getFirst().getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when deleting a non-existing product (404 - not found)")
    void deleteProduct404() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", nonExistingProductId))
                .andExpect(status().isNotFound());
    }

}