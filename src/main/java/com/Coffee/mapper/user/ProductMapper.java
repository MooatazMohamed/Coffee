package com.Coffee.mapper.user;

import com.Coffee.dto.user.response.ProductDetailResponse;
import com.Coffee.dto.user.response.ProductResponse;
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

    @Named("getSmallestSizePrice")
    default Double getSmallestSizePrice(List<ProductSize> sizes) {
        if (sizes == null || sizes.isEmpty()) return 0.0;
        return sizes.stream()
                .filter(size -> !size.getIsDeleted())
                .mapToDouble(ProductSize::getPrice)
                .min()
                .orElse(0.0);
    }

    @Named("mapSizes")
    default List<ProductDetailResponse.SizeOption> mapSizes(List<ProductSize> sizes) {
        if (sizes == null) return null;
        return sizes.stream()
                .filter(size -> !size.getIsDeleted())
                .map(size -> {
                    ProductDetailResponse.SizeOption option = new ProductDetailResponse.SizeOption();
                    option.setId(size.getId());
                    option.setSize(size.getSize());
                    option.setPrice(size.getPrice());
                    option.setIsSelected(false);
                    return option;
                })
                .collect(Collectors.toList());
    }
}
