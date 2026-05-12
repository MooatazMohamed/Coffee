package com.Coffee.dto.admin.response;

import com.Coffee.enums.OrderStatus;
import com.Coffee.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface BranchesOrderView {
    String getOrderNumber();
    String getUserName();
    String getBranchName();
    BigDecimal getTotalPrice();
    OrderType getType();
    LocalDateTime getCreatedAt();
    Long getBranchId();
    OrderStatus getStatus();
}

