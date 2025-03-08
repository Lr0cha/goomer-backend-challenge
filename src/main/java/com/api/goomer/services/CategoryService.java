package com.api.goomer.services;

import com.api.goomer.entities.category.Category;
import com.api.goomer.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public Category create(Category category) {
        return repository.save(category);
    }

    public Category findById(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrado"));
    }

    @Transactional
    public void delete(Long id) {
        Category category = findById(id);
        repository.delete(category);
    }
}
