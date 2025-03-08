package com.api.goomer.web.controllers;

import com.api.goomer.entities.product.Product;
import com.api.goomer.services.ProductService;
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

    @GetMapping(value =  "{id}")
    public ResponseEntity<Page<Product>> getAllProductsByRestaurant(@PathVariable UUID restaurantId, Pageable pageable) {
        return ResponseEntity.ok(service.findProductsByRestaurant(restaurantId,pageable));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(product));
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        service.update(id,product);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Product> deleteProduct(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
