package com.Coffee.dto.admin.response;



import java.util.List;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        String imageUrl,
        CategoryResponseDTO category ,
        List<ProductSizeResponseDTO> productSizes
        ){

        }
