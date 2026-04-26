package com.Coffee.mapper.user;

import com.Coffee.dto.manager.response.OrderItemDTO;
import com.Coffee.dto.manager.response.OrderResponseDTO;
import com.Coffee.dto.user.response.OrderResponse;
import com.Coffee.model.Addon;
import com.Coffee.model.Branch;
import com.Coffee.model.Order;
import com.Coffee.model.OrderItem;
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


    @Mapping(target = "products", source = "orderItems")
    OrderResponseDTO toDTO(Order order);

    @Mapping(target = "productName", source = "productSize.product.name")
    @Mapping(target = "size", source = "productSize.size")
    @Mapping(target = "unitPrice", source = "price")
    @Mapping(target = "itemTotalPrice", source = "totalPrice")
    @Mapping(target = "addons", expression = "java(item.getAddons().stream().map(a -> a.getName()).toList())")
    OrderItemDTO toItemDTO(OrderItem item);

    List<OrderResponseDTO> toDTOList(List<Order> orders);
}
