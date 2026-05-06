package com.Coffee.dto.admin.response;

import com.Coffee.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for displaying recent orders in dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentOrderResponse {
    private Long orderId;
    private String orderNumber;
    private String customerName;
    private String branchName;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private String statusBadge; // "completed", "pending", "preparing", "ready"
}

