package com.api.goomer.web.controllers;

import com.api.goomer.entities.restaurant.Restaurant;
import com.api.goomer.services.RestaurantService;
import com.api.goomer.web.dtos.mapper.RestaurantMapper;
import com.api.goomer.web.dtos.restaurant.RestaurantCreateDto;
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
@RequestMapping("api/restaurants")
@Tag(name = "Restaurante", description = "Todas as operações relativas a cadastro, leitura, atualização e exclusão de um restaurante")
public class RestaurantController {
    @Autowired
    private RestaurantService service;

    @Operation(summary="Recuperar todos os restaurantes",description = "Recurso para recuperar restaurantes cadastrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResponseDto.class)))
            })
    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDto>> findAll(@RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String address,
                                                               Pageable pageable){
       Page<Restaurant> restaurants =  service.findAll(name, address, pageable);
        return ResponseEntity.ok(restaurants.map(RestaurantMapper::toDto));
    }

    @Operation(summary="Recuperar restaurante pelo id",description = "Recurso para recuperar restaurante pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping(value = "{id}")
    public ResponseEntity<Restaurant> findRestaurantById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary="Criar restaurante",description = "Recurso para criar restaurante",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResponseDto.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Restaurante já existe",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantCreateDto restaurantCreateDto){
        Restaurant restaurantCreated = service.create(RestaurantMapper.toRestaurant(restaurantCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantMapper.toDto(restaurantCreated));
    }
    @Operation(summary="Atualizar restaurante pelo id",description = "Recurso para atualizar dados do restaurante",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Recurso atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping(value = "{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable UUID id, @Valid @RequestBody RestaurantCreateDto dto){
        service.update(id,RestaurantMapper.toRestaurant(dto));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary="Deletar restaurante pelo id",description = "Recurso para deletar um restaurante",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Recurso excluido com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
