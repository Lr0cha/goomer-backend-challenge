package com.api.goomer.services;

import com.api.goomer.entities.category.Category;
import com.api.goomer.entities.product.Product;
import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.exceptions.EntityIsNotFoundException;
import com.api.goomer.exceptions.UniqueViolationException;
import com.api.goomer.repositories.ProductRepository;
import com.api.goomer.utils.CreateTestEntity;
import com.api.goomer.web.dtos.mapper.ProductMapper;
import com.api.goomer.web.dtos.product.ProductCreateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;


import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    ProductRepository repository;

    @Mock
    RestaurantService restaurantService;

    @Mock
    CategoryService categoryService;

    @Autowired
    @InjectMocks
    ProductService productService;

    private ProductCreateDto productCreateDto;
    private Restaurant restaurant;
    private Category category;
    private Product product;
    private LocalTime[] times;
    private Pageable pageable;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        pageable = PageRequest.of(0, 10);

        times = new LocalTime[]{LocalTime.of(9, 0), LocalTime.of(12, 30)};

        restaurant = CreateTestEntity.restaurant(UUID.randomUUID());

        category = CreateTestEntity.category(1L);

        productCreateDto = CreateTestEntity.product(category,restaurant);

        product = ProductMapper.toProduct(productCreateDto, restaurant, category);
    }

    @Test
    @DisplayName("should show a product from a restaurant when everything is ok")
    void findProductsByRestaurant200() {
        Product product2 = new Product(UUID.randomUUID(),"Produto B", BigDecimal.valueOf(100.00),restaurant,category);

        when(restaurantService.findById(restaurant.getId())).thenReturn(restaurant);

        List<Product> products = List.of(product,product2);

        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(repository.findAll((Specification<Product>) any(), eq(pageable))).thenReturn(productPage);

        Page<Product> result = productService.findProductsByRestaurant(null, category.getDescription(), restaurant.getId(), pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().contains(product));
    }

    @Test
    @DisplayName("should create a product successfully when everything is ok")
    void create201() {
        when(categoryService.findById(productCreateDto.categoryId())).thenReturn(category);
        when(restaurantService.findById(productCreateDto.restaurantId())).thenReturn(restaurant);

        when(repository.findByProductNameAndRestaurant(anyString(), any(Restaurant.class)))
                .thenReturn(null);

        when(repository.save(any(Product.class))).thenReturn(product);

        Product result = productService.create(productCreateDto);

        assertNotNull(result);
        assertEquals("Produto A", result.getProductName());
        assertEquals(new BigDecimal("49.99"), result.getPrice());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("should throw UniqueViolationException when the product already exists in the restaurant")
    void create409() {
        when(categoryService.findById(category.getId())).thenReturn(category);
        when(restaurantService.findById(restaurant.getId())).thenReturn(restaurant);

        when(repository.findByProductNameAndRestaurant(anyString(), any(Restaurant.class)))
                .thenReturn(new Product());

        UniqueViolationException exception = assertThrows(UniqueViolationException.class, () -> {
            productService.create(productCreateDto);
        });

        assertEquals("Produto 'Produto A' já existe neste restaurante", exception.getMessage());
    }

    @Test
    @DisplayName("should update a product successfully when everything is ok")
    void update204() {
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        when(categoryService.findById(category.getId())).thenReturn(category);
        when(restaurantService.findById(restaurant.getId())).thenReturn(restaurant);

        ProductCreateDto updateDto = new ProductCreateDto(
                "https://example.com/image2.jpg",
                "Produto atualizado",
                new BigDecimal("99.99"),
                1L,
                true,
                "Desconto especial de 50%",
                new BigDecimal("49.99"),
                "Segunda, Quarta, Sexta",
                times,
                restaurant.getId()
        );

        when(repository.save(any(Product.class))).thenReturn(product);

        productService.update(product.getId(), updateDto);

        assertEquals("Produto atualizado", product.getProductName());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals("Desconto especial de 50%", product.getOffer().getPromotionalDescription());
        assertEquals(new BigDecimal("49.99"), product.getOffer().getPromotionalPrice());
        assertEquals("Segunda, Quarta, Sexta", product.getOffer().getPromotionalDays());
        assertEquals("https://example.com/image2.jpg", product.getUrlImage());

        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("should throw UniqueViolationException when the product not found")
    void update404() {
        when(repository.findById(product.getId())).thenReturn(Optional.empty());

        EntityIsNotFoundException exception = assertThrows(EntityIsNotFoundException.class, () -> {
            productService.update(product.getId(), productCreateDto);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("should delete a product sucessfully")
    void delete204() {
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        productService.delete(product.getId());

        verify(repository, times(1)).delete(product);
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when deleting a non-existing product")
    void delete404() {
        when(repository.findById(product.getId())).thenReturn(Optional.empty());

        EntityIsNotFoundException exception = assertThrows(
                EntityIsNotFoundException.class,
                () -> productService.delete(product.getId())
        );
        assertEquals("Produto não encontrado", exception.getMessage());
    }
}