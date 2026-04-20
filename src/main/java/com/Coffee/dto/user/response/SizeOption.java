package com.Coffee.dto.user.response;

import lombok.Data;

@Data
public class SizeOption {
    private Long id;
    private String size;
    private Double price;
    private Boolean isSelected; // For default selection
}

