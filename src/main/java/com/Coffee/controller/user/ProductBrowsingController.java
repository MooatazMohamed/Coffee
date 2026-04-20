package com.Coffee.controller.user;

import com.Coffee.dto.user.request.PriceCalculationResponse;
import com.Coffee.dto.user.request.ProductCustomizationRequest;
import com.Coffee.dto.user.response.CategoryResponse;
import com.Coffee.dto.user.response.ProductDetailResponse;
import com.Coffee.dto.user.response.ProductResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.service.user.ProductBrowsingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
public class ProductBrowsingController {

    private final ProductBrowsingService productBrowsingService;

    // ========== Landing Page ==========

    /**
     * Get featured products for landing page
     * GET /api/user/products/featured
     */
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts() {
        List<ProductResponse> featuredProducts = productBrowsingService.getFeaturedProducts();
        return ResponseEntity.ok(ApiResponse.success("Featured products retrieved successfully", featuredProducts));
    }

    // ========== Category Browsing ==========

    /**
     * Get all categories for browsing
     * GET /api/user/products/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = productBrowsingService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", categories));
    }

    /**
     * Get products by category
     * GET /api/user/products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId) {
        List<ProductResponse> products = productBrowsingService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    // ========== Product Details & Customization ==========

    /**
     * Get product details with sizes and add-ons for customization
     * GET /api/user/products/{productId}/details
     */
    @GetMapping("/{productId}/details")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductDetails(
            @PathVariable Long productId) {
        ProductDetailResponse productDetails = productBrowsingService.getProductDetails(productId);
        return ResponseEntity.ok(ApiResponse.success("Product details retrieved successfully", productDetails));
    }

    /**
     * Calculate dynamic price based on selected size and add-ons
     * POST /api/user/products/calculate-price
     */
    @PostMapping("/calculate-price")
    public ResponseEntity<ApiResponse<PriceCalculationResponse>> calculatePrice(
            @Valid @RequestBody ProductCustomizationRequest request) {
        PriceCalculationResponse price = productBrowsingService.calculatePrice(request);
        return ResponseEntity.ok(ApiResponse.success("Price calculated successfully", price));
    }
}
