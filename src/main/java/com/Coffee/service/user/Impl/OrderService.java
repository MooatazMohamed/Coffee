package com.Coffee.service.user.Impl;

import com.Coffee.dto.user.request.OrderItemAddonRequest;
import com.Coffee.dto.user.request.OrderItemRequest;
import com.Coffee.dto.user.request.PlaceOrderRequest;
import com.Coffee.dto.user.response.OrderResponse;
import com.Coffee.enums.OrderStatus;
import com.Coffee.enums.OrderType;
import com.Coffee.handler.ResourceNotFoundException;
import com.Coffee.mapper.admin.AddonMapper;
import com.Coffee.mapper.user.OrderItemMapper;
import com.Coffee.mapper.user.OrderMapper;
import com.Coffee.model.*;
//import com.Coffee.repository.user.*;
import com.Coffee.repository.admin.BranchRepository;
import com.Coffee.repository.user.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductSizeRepository productSizeRepository;
    private final AddonRepository addonRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final AddonMapper addonMapper;

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request, Long userId) {
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setBranch(branch);
        order.setStatus(OrderStatus.PENDING);
        //XXX
        order.setType(OrderType.ORDER_NOW);
        //XXX
        order.setNotes(request.getNotes());
        order.setCreatedAt(LocalDateTime.now());
        order.setIsDeleted(false);

        BigDecimal orderTotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItemRequest itemRequest : request.getItems()) {
            ProductSize productSize = productSizeRepository
                    .findActiveById(itemRequest.getProductSizeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product size not found"));

            List<Long> addonsIds = itemRequest.getAddons().stream()
                    .map(OrderItemAddonRequest::getAddonId)
                    .toList();

            List<Addon> addons = addonsIds.isEmpty()
                    ? new ArrayList<>()
                    : addonRepository.findAllById(addonsIds);

            if (addons.size() != addonsIds.size()) {
                throw new ResourceNotFoundException("One or more addons not found");
            }

            BigDecimal addonsTotal = addons.stream()
                    .map(Addon::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal unitPrice = productSize.getPrice().add(addonsTotal);
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductSize(productSize);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(unitPrice);
            orderItem.setTotalPrice(itemTotal);
            orderItem.setNotes(itemRequest.getNotes());
            orderItem.setAddons(addons);
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setIsDeleted(false);

            orderItems.add(orderItem);
            orderTotal = orderTotal.add(itemTotal);
        }
        order.setTotalPrice(orderTotal);
        order.setOrderItems(orderItems);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderResponse(saved);
    }

    public List<OrderResponse> getMyOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId);
        return orderMapper.toOrderResponseList(orders);
    }

    public OrderResponse getOrderById(Long orderId, Long userId) throws AccessDeniedException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }
        return orderMapper.toOrderResponse(order);
    }

    private String generateOrderNumber() {
        return "#ORD-" + (System.currentTimeMillis() % 9000 + 1000);
    }
}
