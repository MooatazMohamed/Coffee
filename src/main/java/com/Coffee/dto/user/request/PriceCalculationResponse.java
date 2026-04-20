package com.Coffee.dto.user.request;

import lombok.Data;

import java.util.List;

@Data
public class PriceCalculationResponse {
    private Double basePrice;
    private Double selectedSizePrice;
    private List<AddonPrice> selectedAddons;
    private Double totalPrice;

    @Data
    public static class AddonPrice {
        private Long id;
        private String name;
        private Double price;
    }
}
