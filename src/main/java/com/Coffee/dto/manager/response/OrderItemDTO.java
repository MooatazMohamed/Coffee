package com.Coffee.dto.manager.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private String productName;
    private String size;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal itemTotalPrice;
    private List<String> addons;
}
