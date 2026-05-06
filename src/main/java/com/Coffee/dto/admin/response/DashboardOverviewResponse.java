package com.Coffee.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for complete dashboard overview response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverviewResponse {
    private MetricCardResponse totalRevenue;
    private MetricCardResponse activeBranches;
    private MetricCardResponse totalOrders;
    private MetricCardResponse totalProducts;
    private List<RecentOrderResponse> recentOrders;
}

