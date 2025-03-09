package com.api.goomer.web.controllers;

import com.api.goomer.entities.category.Category;
import com.api.goomer.services.CategoryService;
import com.api.goomer.web.dtos.category.CategoryDto;
import com.api.goomer.web.dtos.mapper.CategoryMapper;
import jakarta.validation.Valid;
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
    public ResponseEntity<Page<CategoryDto>> findAllCategories(Pageable pageable){
        Page<Category> categories = service.findAll(pageable);
        return ResponseEntity.ok(categories.map(CategoryMapper::toDto));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategories(@Valid @RequestBody CategoryDto categoryDto){
        Category categoryCreated = service.create(CategoryMapper.toCategory(categoryDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryMapper.toDto(categoryCreated));
    }

    @DeleteMapping(value =  "{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
