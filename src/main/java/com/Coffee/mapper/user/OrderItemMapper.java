package com.Coffee.mapper.user;

import com.Coffee.dto.user.response.OrderItemResponse;
import com.Coffee.mapper.admin.AddonMapper;
import com.Coffee.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses={AddonMapper.class})
public interface OrderItemMapper {
    @Mapping(source = "productSize.product.name", target = "productName")
    @Mapping(source = "productSize.size", target = "size")
    @Mapping(source = "price", target = "unitPrice")
    @Mapping(source = "addons", target = "addons")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> orderItems);
}
