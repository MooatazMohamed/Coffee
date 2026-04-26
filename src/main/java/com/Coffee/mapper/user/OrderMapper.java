package com.Coffee.mapper.user;

import com.Coffee.dto.user.response.OrderResponse;
import com.Coffee.model.Branch;
import com.Coffee.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" ,uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "branch.name", target = "branchName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "orderItems", target = "items")
    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponseList(List<Order> orders);
}
