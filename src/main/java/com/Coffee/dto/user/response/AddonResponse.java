package com.Coffee.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class AddonResponse {
    private Long id;
    private String name;
    private BigDecimal price;
}
