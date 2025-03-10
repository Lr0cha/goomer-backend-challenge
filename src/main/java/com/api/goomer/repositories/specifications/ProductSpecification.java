package com.api.goomer.repositories.specifications;

import com.api.goomer.entities.product.Product;
import com.api.goomer.entities.restaurant.Restaurant;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> withFilters(String name, String category) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder
                        .lower(root.get("productName")), "%" + name.toLowerCase() + "%"));
            }

            if (category != null && !category.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder
                        .lower(root.get("category").get("description")), "%" + category.toLowerCase() + "%"));
            }


            return predicate;
        };
    }

    public static Specification<Product> withRestaurant(Restaurant restaurant) {
        return (root, query, criteriaBuilder) -> {
            if (restaurant != null) {
                return criteriaBuilder.equal(root.get("restaurant"), restaurant);
            }
            return criteriaBuilder.conjunction();
        };
    }
}
