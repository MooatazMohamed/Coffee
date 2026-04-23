package com.Coffee.dto.user.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class OrderItemRequest {

    @NotNull
    private Long productSizeId;

    @Min(1)
    private int quantity;

    private String notes;

    private List<OrderItemAddonRequest> addons = new ArrayList<>() ;

}
