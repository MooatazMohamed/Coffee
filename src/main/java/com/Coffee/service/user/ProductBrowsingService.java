package com.Coffee.service.user;

import com.Coffee.dto.user.request.PriceCalculationResponse;
import com.Coffee.dto.user.request.ProductCustomizationRequest;
import com.Coffee.dto.user.response.CategoryResponse;
import com.Coffee.dto.user.response.ProductDetailResponse;
import com.Coffee.dto.user.response.ProductResponse;

import java.util.List;

public interface ProductBrowsingService {

    // Landing page - featured products
    List<ProductResponse> getFeaturedProducts();

    // Browse by categories
    List<CategoryResponse> getAllCategories();
    List<ProductResponse> getProductsByCategory(Long categoryId);

    // Product details with customization options
    ProductDetailResponse getProductDetails(Long productId);

    // Dynamic price calculation
    PriceCalculationResponse calculatePrice(ProductCustomizationRequest request);
}
