package com.Coffee.service.admin;

import com.Coffee.dto.admin.response.DashboardOverviewResponse;
import com.Coffee.dto.admin.response.MetricCardResponse;
import com.Coffee.dto.admin.response.RecentOrderResponse;
import com.Coffee.model.Order;
import com.Coffee.repository.admin.BranchRepository;
import com.Coffee.repository.user.OrderRepository;
import com.Coffee.repository.user.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for admin dashboard metrics and overview
 * Responsible for aggregating business metrics and recent activity
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final OrderRepository orderRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    /**
     * Get complete dashboard overview with metrics and recent orders
     * @return DashboardOverviewResponse containing all dashboard data
     */
    public DashboardOverviewResponse getDashboardOverview() {
        DashboardOverviewResponse response = new DashboardOverviewResponse();

        // Calculate metrics
        response.setTotalRevenue(calculateTotalRevenueMetric());
        response.setActiveBranches(calculateActiveBranchesMetric());
        response.setTotalOrders(calculateTotalOrdersMetric());
        response.setTotalProducts(calculateTotalProductsMetric());

        // Fetch recent orders
        response.setRecentOrders(getRecentOrders());

        return response;
    }

    /**
     * Calculate total revenue metric with percentage change
     * Note: Percentage change calculation would require historical data tracking
     */
    private MetricCardResponse calculateTotalRevenueMetric() {
        BigDecimal totalRevenue = orderRepository.getTotalRevenue();
        String formattedValue = formatCurrency(totalRevenue);

        // In a production system, compare with previous period (e.g., last month)
        // For now, return sample percentage
        return new MetricCardResponse(
                "Total Revenue",
                formattedValue,
                0.0  // Would calculate from historical data
        );
    }

    /**
     * Calculate active branches metric
     */
    private MetricCardResponse calculateActiveBranchesMetric() {
        Long activeBranches = branchRepository.getActiveBranchesCount();

        return new MetricCardResponse(
                "Active Branches",
                activeBranches.toString(),
                0.0  // Would compare with previous period
        );
    }

    /**
     * Calculate total orders metric
     */
    private MetricCardResponse calculateTotalOrdersMetric() {
        Long totalOrders = orderRepository.getTotalOrdersCount();

        return new MetricCardResponse(
                "Total Orders",
                formatNumber(totalOrders),
                0.0  // Would compare with previous period
        );
    }

    /**
     * Calculate total products metric
     */
    private MetricCardResponse calculateTotalProductsMetric() {
        Long totalProducts = productRepository.getTotalProductsCount();

        return new MetricCardResponse(
                "Total Products",
                totalProducts.toString(),
                0.0  // No change if static catalog
        );
    }

    /**
     * Get recent orders for dashboard display (last 10)
     */
    private List<RecentOrderResponse> getRecentOrders() {
        List<Order> recentOrders = orderRepository.getRecentOrders();

        return recentOrders.stream()
                .map(this::mapToRecentOrderDto)
                .collect(Collectors.toList());
    }

    /**
     * Map Order entity to RecentOrderDto
     */
    private RecentOrderResponse mapToRecentOrderDto(Order order) {
        return new RecentOrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getUser() != null ? order.getUser().getName() : "Unknown",
                order.getBranch() != null ? order.getBranch().getName() : "Unknown",
                order.getCreatedAt(),
                order.getTotalPrice(),
                order.getStatus(),
                mapStatusToStatusBadge(order.getStatus().toString())
        );
    }

    /**
     * Map order status to badge style identifier
     */
    private String mapStatusToStatusBadge(String status) {
        return switch (status) {
            case "COMPLETED" -> "completed";
            case "PENDING" -> "pending";
            case "PREPARING" -> "preparing";
            case "READY" -> "ready";
            case "CANCELLED" -> "cancelled";
            default -> "unknown";
        };
    }

    /**
     * Format currency value for display
     */
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "$" + df.format(amount);
    }

    /**
     * Format large numbers for display (e.g., 1,234)
     */
    private String formatNumber(Long number) {
        if (number == null) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(number);
    }
}

