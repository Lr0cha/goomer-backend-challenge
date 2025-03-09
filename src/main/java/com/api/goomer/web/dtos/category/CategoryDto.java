package com.api.goomer.web.dtos.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        @NotNull
        @Size(min = 1, max = 255)
        String description
) {}
