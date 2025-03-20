package com.api.goomer.services;

import com.api.goomer.entities.category.Category;
import com.api.goomer.exceptions.EntityIsNotFoundException;
import com.api.goomer.exceptions.UniqueViolationException;
import com.api.goomer.repositories.CategoryRepository;
import com.api.goomer.utils.CreateTestEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    CategoryRepository repository;

    @Autowired
    @InjectMocks
    CategoryService categoryService;

    private static Category category;
    private static Long categoryId;

    @BeforeAll
    static void createCategory(){
        category = CreateTestEntity.category(1L);
        categoryId = category.getId();
    }

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should create a category successfully when everything is ok")
    void create201() {
        when(repository.findByDescription("Nova Categoria")).thenReturn(null);
        when(repository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.create(category);

        assertNotNull(result);
        assertEquals("Categoria A", result.getDescription());
        verify(repository, times(1)).save(category);
    }

    @Test
    @DisplayName("should throw UniqueViolationException when the category already exists")
    void create409_whenCategoryExists() {

        when(repository.findByDescription("Categoria A")).thenReturn(new Category());

        UniqueViolationException exception = assertThrows(
                UniqueViolationException.class,
                () -> categoryService.create(category)
        );
        assertEquals("Categoria 'Categoria A' já existe", exception.getMessage());
    }

    @Test
    @DisplayName("should find a category by id successfully")
    void findById200() {
        category.setId(categoryId);
        when(repository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(categoryId);

        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals("Categoria A", result.getDescription());
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when category not found")
    void findById404() {
        when(repository.findById(categoryId)).thenReturn(Optional.empty());

        EntityIsNotFoundException exception = assertThrows(
                EntityIsNotFoundException.class,
                () -> categoryService.findById(categoryId)
        );
        assertEquals("Categoria não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("should delete a category successfully")
    void delete204() {
        when(repository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.delete(categoryId);

        verify(repository, times(1)).delete(category);
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when deleting a non-existing category")
    void delete404() {
        when(repository.findById(categoryId)).thenReturn(Optional.empty());

        EntityIsNotFoundException exception = assertThrows(
                EntityIsNotFoundException.class,
                () -> categoryService.delete(categoryId)
        );
        assertEquals("Categoria não encontrada", exception.getMessage());
    }
}
