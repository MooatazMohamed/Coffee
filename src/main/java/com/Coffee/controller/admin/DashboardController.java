package com.Coffee.controller.admin;

import com.Coffee.dto.admin.response.DashboardOverviewResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.service.admin.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for admin dashboard endpoints
 * Provides metrics, overview, and analytics data for dashboard display
 */
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Get complete dashboard overview with all metrics and recent orders
     * GET /api/admin/dashboard/overview
     *
     * @return ResponseEntity containing DashboardOverviewResponse wrapped in ApiResponse
     */
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<DashboardOverviewResponse>> getDashboardOverview() {
        DashboardOverviewResponse overview = dashboardService.getDashboardOverview();
        return ResponseEntity.ok(
                ApiResponse.success("Dashboard overview retrieved successfully", overview)
        );
    }
}

