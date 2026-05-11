package com.Coffee.dto.admin.request;

import com.Coffee.dto.admin.response.CategoryResponseDTO;
import com.Coffee.dto.admin.response.ProductSizeResponseDTO;
import com.Coffee.model.Category;
import com.Coffee.model.ProductSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProductRequestDTO(
        @NotBlank(message = "Product Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,
        @NotBlank(message = "Product Description is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String description,
         MultipartFile imageUrl,
        Boolean isAvailable,
        //String imageUrl,
        // @NotEmpty(message = "Product Category is required ")
        @NotNull(message = "Product Category is required")
        CategoryResponseDTO category ,
        @NotEmpty(message = "Product Properties  is required  at Least One Property ")
        List<ProductSizeResponseDTO> productSizes
        ){

        }
