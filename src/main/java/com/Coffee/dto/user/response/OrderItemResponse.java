package com.Coffee.dto.user.response;

import com.Coffee.dto.admin.response.AddonResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private String productName;
    private String size;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String notes;
    private List<AddonResponse> addons;
}
