package com.Coffee.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for individual metric card (e.g., Total Revenue, Active Branches)
 */
@Data
@AllArgsConstructor
public class MetricCardResponse {
    private String label;         // e.g., "Total Revenue", "Active Branches"
    private String value;         // e.g., "$24,560", "12"
    private Double changePercent; // e.g., 12.5, -2.3
    private String icon;          // Icon type identifier

    public MetricCardResponse(String label, String value, Double changePercent) {
        this.label = label;
        this.value = value;
        this.changePercent = changePercent;
    }
}

