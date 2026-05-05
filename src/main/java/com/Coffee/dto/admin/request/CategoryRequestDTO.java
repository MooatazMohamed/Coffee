package com.Coffee.dto.admin.request;

import com.Coffee.dto.admin.response.AddonResponse;
import com.Coffee.model.Addon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CategoryRequestDTO(
        @NotBlank(message = "Category Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,
        String imageUrl,
        @NotEmpty(message = "At least one addon must be selected")
        List<Long> addonList) {
}
