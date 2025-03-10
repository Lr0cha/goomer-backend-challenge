package com.api.goomer.services;

import com.api.goomer.entities.category.Category;
import com.api.goomer.entities.product.Product;
import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.exceptions.EntityIsNotFoundException;
import com.api.goomer.exceptions.UniqueViolationException;
import com.api.goomer.repositories.ProductRepository;
import com.api.goomer.web.dtos.mapper.ProductMapper;
import com.api.goomer.web.dtos.product.ProductCreateDto;
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

    @Autowired
    CategoryService categoryService;

    @Transactional(readOnly = true)
    public Page<Product> findProductsByRestaurant(UUID restaurantId, Pageable pageable) {
        Restaurant restaurant = returnRestaurantById(restaurantId);
        return repository.findByRestaurant(restaurant,pageable);
    }

    @Transactional
    public Product create(ProductCreateDto createDto) {
        Restaurant restaurant = returnRestaurantById(createDto.restaurantId());
        Category category = returnCategoryById(createDto.categoryId());
        Product product = ProductMapper.toProduct(createDto, restaurant, category);

        if(repository.findByProductNameAndRestaurant(product.getProductName(), product.getRestaurant()) != null){
            throw new UniqueViolationException(String.format("Produto '%s' já existe neste restaurante", product.getProductName()));
        }

        return repository.save(product);
    }

    public Product findById(UUID id){
        return repository.findById(id).orElseThrow(() -> new EntityIsNotFoundException("Produto não encontrado"));
    }

    @Transactional
    public void update(UUID id, ProductCreateDto createDto) {
        Product product = findById(id); // produto que será atualizado

        Restaurant restaurant = returnRestaurantById(createDto.restaurantId());
        Category category = returnCategoryById(createDto.categoryId());

        Product updatedProduct = ProductMapper.toProduct(createDto, restaurant, category);

        product.setUrlImage(updatedProduct.getUrlImage());
        product.setProductName(updatedProduct.getProductName());
        product.setPrice(updatedProduct.getPrice());
        product.setCategory(updatedProduct.getCategory());
        product.setOffer(updatedProduct.getOffer());
        repository.save(product);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    // funções para resgatar categoria e restaurante relacionados ao produto
    private Restaurant returnRestaurantById(UUID id){
        return restaurantService.findById(id);
    }

    private Category returnCategoryById(long id){
        return categoryService.findById(id);
    }

}
