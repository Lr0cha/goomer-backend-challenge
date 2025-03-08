package com.api.goomer.repositories;

import com.api.goomer.entities.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
