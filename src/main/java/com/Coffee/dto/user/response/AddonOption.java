package com.Coffee.dto.user.response;

import lombok.Data;

@Data
public class AddonOption {
    private Long id;
    private String name;
    private Double price;
    private Boolean isAvailable;
}

