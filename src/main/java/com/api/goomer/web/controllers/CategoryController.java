package com.api.goomer.web.controllers;

import com.api.goomer.entities.category.Category;
import com.api.goomer.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<Page<Category>> findAllCategories(Pageable pageable){
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Category> createCategories(@RequestBody Category category){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(category));
    }

    @DeleteMapping(value =  "{id}")
    public ResponseEntity<Page<Category>> deleteCategoryById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
