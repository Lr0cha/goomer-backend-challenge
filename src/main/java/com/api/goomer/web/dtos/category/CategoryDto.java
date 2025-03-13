package com.api.goomer.web.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        @NotBlank
        @Size(min = 3, max = 100)
        String description
) {}
