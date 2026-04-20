package com.Coffee.service.user.Impl;

import com.Coffee.dto.user.request.PriceCalculationResponse;
import com.Coffee.dto.user.request.ProductCustomizationRequest;
import com.Coffee.dto.user.response.CategoryResponse;
import com.Coffee.dto.user.response.ProductDetailResponse;
import com.Coffee.dto.user.response.ProductResponse;
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
        // Verify category exists
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        List<Product> products = productRepository.findByCategoryIdAndIsAvailableTrueAndIsDeletedFalse(categoryId);
        return productMapper.toResponseList(products);
    }

    @Override
    public ProductDetailResponse getProductDetails(Long productId) {
        Product product = productRepository.findByIdWithSizes(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductDetailResponse response = productMapper.toDetailResponse(product);

        // Get add-ons for this product's category
        if (product.getCategory() != null) {
            List<Addon> categoryAddons = addonRepository.findAddonsByCategoryId(product.getCategory().getId());
            List<ProductDetailResponse.AddonOption> addonOptions = categoryAddons.stream()
                    .map(this::mapToAddonOption)
                    .collect(Collectors.toList());
            response.setAddons(addonOptions);
        }

        // Set default selected size (smallest price)
        if (response.getSizes() != null && !response.getSizes().isEmpty()) {
            ProductDetailResponse.SizeOption defaultSize = response.getSizes().stream()
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

        // Get selected size price
        ProductSize selectedSize = productSizeRepository.findById(request.getSelectedSizeId())
                .orElseThrow(() -> new ResourceNotFoundException("Size not found"));

        response.setSelectedSizePrice(selectedSize.getPrice());
        response.setBasePrice(selectedSize.getPrice()); // Base is the size price

        // Calculate add-ons total
        if (request.getSelectedAddonIds() != null && !request.getSelectedAddonIds().isEmpty()) {
            List<Addon> selectedAddons = addonRepository.findByIdInAndIsAvailableTrueAndIsDeletedFalse(
                    request.getSelectedAddonIds()
            );

            List<PriceCalculationResponse.AddonPrice> addonPrices = selectedAddons.stream()
                    .map(this::mapToAddonPrice)
                    .collect(Collectors.toList());

            response.setSelectedAddons(addonPrices);

            double addonsTotal = selectedAddons.stream()
                    .mapToDouble(addon -> addon.getPrice().doubleValue())
                    .sum();

            response.setTotalPrice(selectedSize.getPrice() + addonsTotal);
        } else {
            response.setSelectedAddons(List.of());
            response.setTotalPrice(selectedSize.getPrice());
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

    private ProductDetailResponse.AddonOption mapToAddonOption(Addon addon) {
        ProductDetailResponse.AddonOption option = new ProductDetailResponse.AddonOption();
        option.setId(addon.getId());
        option.setName(addon.getName());
        option.setPrice(addon.getPrice().doubleValue());
        option.setIsAvailable(addon.isAvailable());
        return option;
    }

    private PriceCalculationResponse.AddonPrice mapToAddonPrice(Addon addon) {
        PriceCalculationResponse.AddonPrice addonPrice = new PriceCalculationResponse.AddonPrice();
        addonPrice.setId(addon.getId());
        addonPrice.setName(addon.getName());
        addonPrice.setPrice(addon.getPrice().doubleValue());
        return addonPrice;
    }
}
