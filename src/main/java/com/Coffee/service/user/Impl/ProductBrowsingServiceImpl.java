package com.Coffee.service.user.Impl;

import com.Coffee.dto.user.response.PriceCalculationResponse;
import com.Coffee.dto.user.response.AddonOption;
import com.Coffee.dto.user.response.AddonPrice;
import com.Coffee.dto.user.response.CategoryResponse;
import com.Coffee.dto.user.response.PriceCalculationResponse;
import com.Coffee.dto.user.response.ProductDetailResponse;
import com.Coffee.dto.user.response.ProductResponse;
import com.Coffee.dto.user.response.SizeOption;
import com.Coffee.dto.user.request.ProductCustomizationRequest;
import com.Coffee.handler.ResourceNotFoundException;
import com.Coffee.mapper.user.ProductMapper;
import com.Coffee.model.Addon;
import com.Coffee.model.Category;
import com.Coffee.model.Product;
import com.Coffee.model.ProductSize;
import com.Coffee.repository.user.AddonRepository;
import com.Coffee.repository.user.CategoryRepository;
import com.Coffee.repository.user.ProductRepository;
import com.Coffee.repository.user.ProductSizeRepository;
import com.Coffee.service.user.ProductBrowsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductBrowsingServiceImpl implements ProductBrowsingService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSizeRepository productSizeRepository;
    private final AddonRepository addonRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> getFeaturedProducts() {
        List<Product> featuredProducts = productRepository.findTop8ByIsAvailableTrueAndIsDeletedFalseOrderByCreatedAtDesc();
        return productMapper.toResponseList(featuredProducts);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findByIsDeletedFalseOrderByName();
        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        // Verify category exists and is not soft-deleted
        categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        List<Product> products = productRepository.findByCategoryIdAndIsAvailableTrueAndIsDeletedFalse(categoryId);
        return productMapper.toResponseList(products);
    }

    @Override
    public ProductDetailResponse getProductDetails(Long productId) {
        Product product = productRepository.findByIdWithSizes(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Check product is available
        if (!Boolean.TRUE.equals(product.getIsAvailable())) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        ProductDetailResponse response = productMapper.toDetailResponse(product);

        // Get add-ons for this product's category
        if (product.getCategory() != null) {
            List<Addon> categoryAddons = addonRepository.findAddonsByCategoryId(product.getCategory().getId());
            List<AddonOption> addonOptions = categoryAddons.stream()
                    .map(this::mapToAddonOption)
                    .collect(Collectors.toList());
            response.setAddons(addonOptions);
        } else {
            response.setAddons(List.of());
        }

        // Set default selected size (smallest price)
        if (response.getSizes() != null && !response.getSizes().isEmpty()) {
            SizeOption defaultSize = response.getSizes().stream()
                    .min((s1, s2) -> Double.compare(s1.getPrice(), s2.getPrice()))
                    .orElse(null);
            if (defaultSize != null) {
                defaultSize.setIsSelected(true);
            }
        }

        return response;
    }

    @Override
    public PriceCalculationResponse calculatePrice(ProductCustomizationRequest request) {
        PriceCalculationResponse response = new PriceCalculationResponse();

        // Validate product exists and is available
        Product product = productRepository.findByIdAndAvailable(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        // Get selected size price, excluding soft-deleted sizes
        ProductSize selectedSize = productSizeRepository.findByIdWithProduct(request.getSelectedSizeId())
                .orElseThrow(() -> new ResourceNotFoundException("Size not found"));

        // Validate size belongs to the requested product
        if (!selectedSize.getProduct().getId().equals(request.getProductId())) {
            throw new ResourceNotFoundException("Size does not belong to the requested product");
        }

        response.setSelectedSizePrice(selectedSize.getPrice().doubleValue());
        response.setBasePrice(selectedSize.getPrice().doubleValue()); // Base is the size price

        // Calculate add-ons total
        if (request.getSelectedAddonIds() != null && !request.getSelectedAddonIds().isEmpty()) {
            // Get all valid addons for this product's category
            List<Addon> validCategoryAddons = product.getCategory() != null
                    ? addonRepository.findAddonsByCategoryId(product.getCategory().getId())
                    : List.of();

            List<Addon> selectedAddons = addonRepository.findByIdInAndIsAvailableTrueAndIsDeletedFalse(
                    request.getSelectedAddonIds()
            );

            // Validate all selected addons belong to the product's category
            for (Addon addon : selectedAddons) {
                boolean isValid = validCategoryAddons.stream()
                        .anyMatch(ca -> ca.getId().equals(addon.getId()));
                if (!isValid) {
                    throw new ResourceNotFoundException("Addon not available for this product's category");
                }
            }

            List<AddonPrice> addonPrices = selectedAddons.stream()
                    .map(this::mapToAddonPrice)
                    .collect(Collectors.toList());

            response.setSelectedAddons(addonPrices);

            double addonsTotal = selectedAddons.stream()
                    .mapToDouble(addon -> addon.getPrice().doubleValue())
                    .sum();

            response.setTotalPrice(selectedSize.getPrice().doubleValue() + addonsTotal);
        } else {
            response.setSelectedAddons(List.of());
            response.setTotalPrice(selectedSize.getPrice().doubleValue());
        }

        return response;
    }

    // Helper methods
    private CategoryResponse mapToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setImageUrl(category.getImageUrl());
        return response;
    }

    private AddonOption mapToAddonOption(Addon addon) {
        AddonOption option = new AddonOption();
        option.setId(addon.getId());
        option.setName(addon.getName());
        option.setPrice(addon.getPrice().doubleValue());
        option.setIsAvailable(addon.getIsAvailable());
        return option;
    }

    private AddonPrice mapToAddonPrice(Addon addon) {
        AddonPrice addonPrice = new AddonPrice();
        addonPrice.setId(addon.getId());
        addonPrice.setName(addon.getName());
        addonPrice.setPrice(addon.getPrice().doubleValue());
        return addonPrice;
    }
}
