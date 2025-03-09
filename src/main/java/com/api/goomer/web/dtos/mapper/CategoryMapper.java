package com.api.goomer.web.dtos.mapper;

import com.api.goomer.entities.category.Category;
import com.api.goomer.web.dtos.category.CategoryDto;

public class CategoryMapper {

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getDescription());
    }

    public static Category toCategory(CategoryDto dto) {
        return new Category(dto.description());
    }
}
