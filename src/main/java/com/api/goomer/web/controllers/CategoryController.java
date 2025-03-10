package com.api.goomer.web.controllers;

import com.api.goomer.entities.category.Category;
import com.api.goomer.services.CategoryService;
import com.api.goomer.web.dtos.category.CategoryDto;
import com.api.goomer.web.dtos.mapper.CategoryMapper;
import com.api.goomer.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/categories")
@Tag(name = "Categoria", description = "Todas as operações relativas a cadastro, leitura e exclusão de uma categoria do produto")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @Operation(summary="Recuperar todas as categorias",description = "Recurso para recuperar categorias cadastradas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class)))
            })
    @GetMapping
    public ResponseEntity<Page<CategoryDto>> findAllCategories(Pageable pageable){
        Page<Category> categories = service.findAll(pageable);
        return ResponseEntity.ok(categories.map(CategoryMapper::toDto));
    }

    @Operation(summary="Criar uma categoria",description = "Recurso para criar categorias",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategories(@Valid @RequestBody CategoryDto categoryDto){
        Category categoryCreated = service.create(CategoryMapper.toCategory(categoryDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryMapper.toDto(categoryCreated));
    }

    @Operation(summary="Deletar uma categoria",description = "Recurso para deletar uma categoria pelo id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Recurso deletado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @DeleteMapping(value =  "{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
