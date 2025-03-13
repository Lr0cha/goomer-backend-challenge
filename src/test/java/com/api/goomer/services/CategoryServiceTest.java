package com.api.goomer.services;

import com.api.goomer.entities.category.Category;
import com.api.goomer.exceptions.EntityIsNotFoundException;
import com.api.goomer.exceptions.UniqueViolationException;
import com.api.goomer.repositories.CategoryRepository;
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

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should create a category successfully when everything is ok")
    void create201() {
        Category category = new Category("Nova Categoria");


        when(repository.findByDescription("Nova Categoria")).thenReturn(null);
        when(repository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.create(category);

        assertNotNull(result);
        assertEquals("Nova Categoria", result.getDescription());
        verify(repository, times(1)).save(category);
    }

    @Test
    @DisplayName("should throw UniqueViolationException when the category already exists")
    void create409_whenCategoryExists() {
        Category category = new Category("Categoria Existente");

        when(repository.findByDescription("Categoria Existente")).thenReturn(new Category());

        UniqueViolationException exception = assertThrows(
                UniqueViolationException.class,
                () -> categoryService.create(category)
        );
        assertEquals("Categoria 'Categoria Existente' já existe", exception.getMessage());
    }

    @Test
    @DisplayName("should find a category by id successfully")
    void findById200() {
        Long id = 1L;

        Category category = new Category(id,"Categoria 1");

        when(repository.findById(id)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Categoria 1", result.getDescription());
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when category not found")
    void findById404() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        EntityIsNotFoundException exception = assertThrows(
                EntityIsNotFoundException.class,
                () -> categoryService.findById(id)
        );
        assertEquals("Categoria não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("should delete a category successfully")
    void delete204() {
        Long id = 1L;
        Category category = new Category(id, "Category to delete");

        when(repository.findById(id)).thenReturn(Optional.of(category));

        categoryService.delete(id);

        verify(repository, times(1)).delete(category);
    }

    @Test
    @DisplayName("should throw EntityIsNotFoundException when deleting a non-existing category")
    void delete404() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        EntityIsNotFoundException exception = assertThrows(
                EntityIsNotFoundException.class,
                () -> categoryService.delete(id)
        );
        assertEquals("Categoria não encontrada", exception.getMessage());
    }
}
