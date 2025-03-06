package com.api.goomer.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "url_image")
    private String urlImage;

    private String description;

    private BigDecimal price;

    private Category category;

    @Column(name = "promotional_description")
    private String promotionalDescription;

    @Column(name = "promotional_price")
    private String promotionalPrice;

    @Column(name = "is_on_promotion")
    private Boolean isOnPromotion;
}
