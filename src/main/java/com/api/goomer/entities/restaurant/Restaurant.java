package com.api.goomer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.util.UUID;
@Table(name = "restaurants")
public class Restaurant {
    private UUID id;

    @Column(name = "url_image")
    private String urlImage;

    private String name;

    private String address;
}

