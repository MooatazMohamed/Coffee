package com.Coffee.controller.user;

import com.Coffee.dto.manager.response.OrderResponseDTO;
import com.Coffee.dto.user.request.PlaceOrderRequest;
import com.Coffee.dto.user.response.OrderResponse;
import com.Coffee.enums.OrderStatus;
import com.Coffee.handler.ApiResponse;
import com.Coffee.model.UserPrincipal;
import com.Coffee.service.security.MyUserDetailsService;
import com.Coffee.service.user.Impl.OrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @Valid @RequestBody PlaceOrderRequest request ,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        Long userId = userPrincipal.getId();
        OrderResponse order = orderService.placeOrder(request,userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully",order));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal UserPrincipal userPrincipal){
        Long userId = userPrincipal.getId();
        List<OrderResponse> orders = orderService.getMyOrders(userId);
        return ResponseEntity
                .ok(ApiResponse.success("Orders retrieved successfully",orders));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) throws AccessDeniedException {
        Long userId = userPrincipal.getId();
        OrderResponse order = orderService.getOrderById(orderId,userId);
        return ResponseEntity
                .ok(ApiResponse.success("Order retrieved successfully",order));
    }

    @GetMapping("/getOrdersBranch")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>>
    getOrdersForOneBranchByBranchId(@RequestHeader("Authorization") String token ,
                                    @RequestParam(required = false) String search){
        return ResponseEntity.ok(orderService.getOrdersForOneBranchByBranchId(token , search));
    }

    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<ApiResponse<String>> changeStatusOfORder(@PathVariable Long id , @RequestParam OrderStatus orderStatus){
        return ResponseEntity.ok(orderService.changeStatusOfOrder(id, orderStatus));
    }

    @GetMapping("/getOrdersBranchReady")
    public ApiResponse<List<OrderResponseDTO>> getReadyOrders(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String search) {
        return orderService.getOrdersForOneBranchByBranchIdAndStatusIsREADY(token, search);
    }
}
