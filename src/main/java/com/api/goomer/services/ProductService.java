package com.api.goomer.services;

import com.api.goomer.entities.product.Product;
import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    ProductRepository repository;

    @Autowired
    RestaurantService restaurantService;

    @Transactional(readOnly = true)
    public Page<Product> findProductsByRestaurant(UUID restaurantId, Pageable pageable) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        return repository.findByRestaurant(restaurant,pageable);
    }

    @Transactional
    public Product create(Product product) {

        return repository.save(product);
    }

    public Product findByid(UUID id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @Transactional
    public void update(UUID id, Product updatedProduct) {
        Product product = findByid(id);
        product.setProductName(updatedProduct.getProductName());
        product.setPrice(updatedProduct.getPrice());
        //...
        repository.save(product);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
