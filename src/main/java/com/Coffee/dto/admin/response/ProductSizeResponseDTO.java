package com.Coffee.dto.admin.response;

import java.math.BigDecimal;

public record ProductSizeResponseDTO(
        Long id,
         String size,
        BigDecimal price) {
}
