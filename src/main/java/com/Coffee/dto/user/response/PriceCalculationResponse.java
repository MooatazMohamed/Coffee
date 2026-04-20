package com.Coffee.dto.user.response;

import lombok.Data;

import java.util.List;

@Data
public class PriceCalculationResponse {
    private Double basePrice;
    private Double selectedSizePrice;
    private List<AddonPrice> selectedAddons;
    private Double totalPrice;
}

