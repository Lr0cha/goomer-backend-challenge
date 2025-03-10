package com.api.goomer.web.controllers;

import com.api.goomer.entities.product.Product;
import com.api.goomer.services.ProductService;
import com.api.goomer.web.dtos.mapper.ProductMapper;
import com.api.goomer.web.dtos.product.ProductCreateDto;
import com.api.goomer.web.dtos.product.ProductResponseDto;
import com.api.goomer.web.dtos.restaurant.RestaurantResponseDto;
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

import java.util.UUID;

@RestController
@RequestMapping("api/products")
@Tag(name = "Produto", description = "Todas as operações relativas a cadastro, leitura, atualização e exclusão de um produto")
public class ProductController {

    @Autowired
    ProductService service;

    @Operation(summary="Recuperar todos os produtos do restaurante",description = "Recurso para recuperar produtos do restaurante",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping(value =  "{restaurantId}")
    public ResponseEntity<Page<ProductResponseDto>> getAllProductsByRestaurant(@PathVariable UUID restaurantId, Pageable pageable) {
        Page<Product> products = service.findProductsByRestaurant(restaurantId,pageable);
        return ResponseEntity.ok(products.map(ProductMapper::toDto));
    }

    @Operation(summary="Criar um produto do restaurante",description = "Recurso para criar produto do restaurante",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Restaurante e/ou categoria não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductCreateDto createDto) {
        Product newProduct = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toDto(newProduct));
    }

    @Operation(summary="Atualizar um produto do restaurante",description = "Recurso para atualizar produto do restaurante",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Recurso atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Restaurante e/ou categoria não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping(value =  "{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductCreateDto createDto) {
        service.update(id,createDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary="Deletar um produto do restaurante",description = "Recurso para deletar produto do restaurante",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Recurso deletado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @DeleteMapping(value =  "{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
