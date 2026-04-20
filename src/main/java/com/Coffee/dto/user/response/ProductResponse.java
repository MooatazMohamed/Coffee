package com.Coffee.dto.user.response;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Double basePrice; // Smallest size price
    private Boolean isAvailable;
}
