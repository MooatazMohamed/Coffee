package com.Coffee.mapper.user;

import com.Coffee.dto.user.response.AddonOption;
import com.Coffee.dto.user.response.ProductDetailResponse;
import com.Coffee.dto.user.response.ProductResponse;
import com.Coffee.dto.user.response.SizeOption;
import com.Coffee.model.Product;
import com.Coffee.model.ProductSize;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productSizes", target = "basePrice", qualifiedByName = "getSmallestSizePrice")
    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @Mapping(source = "productSizes", target = "sizes", qualifiedByName = "mapSizes")
    @Mapping(target = "addons", ignore = true) // Will be set separately
    ProductDetailResponse toDetailResponse(Product product);

    // Automatic mapping for ProductSize -> SizeOption
    @Mapping(target = "isSelected", constant = "false")
    SizeOption toSizeOption(ProductSize size);

    // Helper methods for complex logic
    @Named("getSmallestSizePrice")
    default Double getSmallestSizePrice(List<ProductSize> sizes) {
        if (sizes == null || sizes.isEmpty()) return 0.0;
        return sizes.stream()
                .filter(size -> !Boolean.TRUE.equals(size.getIsDeleted()))
                .mapToDouble(size -> size.getPrice().doubleValue())
                .min()
                .orElse(0.0);
    }

    @Named("mapSizes")
    default List<SizeOption> mapSizes(List<ProductSize> sizes) {
        if (sizes == null) return null;
        return sizes.stream()
                .filter(size -> !Boolean.TRUE.equals(size.getIsDeleted()))
                .map(this::toSizeOption)
                .collect(Collectors.toList());
    }
}
