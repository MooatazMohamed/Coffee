package com.Coffee.dto.manager.response;

import com.Coffee.enums.OrderStatus;
import com.Coffee.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;
    private String orderNumber;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OrderType type;
    private OrderStatus status;
    private LocalDateTime pickupTime;
    private List<OrderItemDTO> products;
}
