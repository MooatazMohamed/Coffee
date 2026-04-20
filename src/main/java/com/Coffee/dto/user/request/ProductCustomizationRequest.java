package com.Coffee.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductCustomizationRequest {
    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "selectedSizeId is required")
    private Long selectedSizeId;

    private List<Long> selectedAddonIds;
}
