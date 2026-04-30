package com.Coffee.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String status;
    private String branchName;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private String notes;
    private List<OrderItemResponse> items;
}
