package com.api.goomer.web.controllers;

import com.api.goomer.entities.product.Product;
import com.api.goomer.services.ProductService;
import com.api.goomer.web.dtos.mapper.ProductMapper;
import com.api.goomer.web.dtos.product.ProductCreateDto;
import com.api.goomer.web.dtos.product.ProductResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/products")
public class ProductController {

    @Autowired
    ProductService service;

    @GetMapping(value =  "{restaurantId}")
    public ResponseEntity<Page<ProductResponseDto>> getAllProductsByRestaurant(@PathVariable UUID restaurantId, Pageable pageable) {
        Page<Product> products = service.findProductsByRestaurant(restaurantId,pageable);
        return ResponseEntity.ok(products.map(ProductMapper::toDto));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductCreateDto createDto) {
        Product newProduct = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toDto(newProduct));
    }

    @PutMapping(value =  "{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductCreateDto createDto) {
        service.update(id,createDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value =  "{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
