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
}

