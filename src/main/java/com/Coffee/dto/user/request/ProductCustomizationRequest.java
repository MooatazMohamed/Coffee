package com.Coffee.dto.user.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductCustomizationRequest {
    private Long productId;
    private Long selectedSizeId;
    private List<Long> selectedAddonIds;
}
