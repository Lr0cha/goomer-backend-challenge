package com.api.goomer.web.controllers;

import com.api.goomer.entities.category.Category;
import com.api.goomer.repositories.CategoryRepository;
import com.api.goomer.services.CategoryService;
import com.api.goomer.utils.CreateTestEntity;
import com.api.goomer.web.dtos.category.CategoryDto;
import com.api.goomer.web.dtos.mapper.CategoryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryControllerIT {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryDto newCategory;
    private Category category1;

    @BeforeEach
    void setup(){
        category1 = CreateTestEntity.category();
        newCategory = new CategoryDto("Categoria B");
        categoryService.create(category1);
    }

    @AfterEach
    void cleanUp(){
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("should create a category successfully when everything is ok (201 - created)")
    public void createCategory201() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("Categoria B")));
    }

    @Test
    @DisplayName("should throw UniqueViolationException when the category already exists (409 - conflict)")
    public void createCategory409() throws Exception {
        categoryService.create(CategoryMapper.toCategory(newCategory));
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newCategory)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("should throw MethodArgumentNotValidException when data field is invalid (422 - unprocessable entity)")
    public void createCategory422() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CategoryDto(""))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("should find all categories successfully (200 - ok)")
    public void findAllCategories200() throws Exception {

        mockMvc.perform(get("/api/categories")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description", is(category1.getDescription())));
    }

    @Test
    @DisplayName("should delete a category by id successfully (204 - no content)")
    public void deleteCategory204() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}", category1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should throw EntityIsNotFound when deleting a non-existing category (404 - not found)")
    public void deleteCategory404() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}",5L))
                .andExpect(status().isNotFound());
    }
}
