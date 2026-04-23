package com.Coffee.dto.user.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PlaceOrderRequest {
    @NotNull(message = "Branch is required")
    private Long branchId;

    private String notes;

    @NotEmpty(message = "Order must have at least one item")
    private List<OrderItemRequest> items;
}
