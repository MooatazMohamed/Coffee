package com.Coffee.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchesOrder {

    private String orderNumber;
    private String userName;
    private String branchName;
    private BigDecimal totalPrice;
    private String type;
    private LocalDateTime createdAt;
    private Long branchId;
    private String status;
}