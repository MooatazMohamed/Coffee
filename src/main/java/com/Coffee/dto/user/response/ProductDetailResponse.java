package com.Coffee.dto.user.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isAvailable;
    private List<SizeOption> sizes;
    private List<AddonOption> addons;

    @Data
    public static class SizeOption {
        private Long id;
        private String size;
        private Double price;
        private Boolean isSelected; // For default selection
    }

    @Data
    public static class AddonOption {
        private Long id;
        private String name;
        private Double price;
        private Boolean isAvailable;
    }
}
