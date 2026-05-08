package com.Coffee.mapper.admin;

import com.Coffee.dto.admin.request.ProductRequestDTO;
import com.Coffee.dto.admin.response.ProductResponseDTO;
import com.Coffee.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring" )
public interface AdminProductMapper {

    @Mapping(target = "imageUrl", ignore = true)
    Product productDtoToEntity(ProductRequestDTO dto);

    @Mapping(target = "imageUrl", expression = "java(com.Coffee.helper.ImageUrlHelper." +
            "buildImageUrl(entity.getImageUrl()))")
    ProductResponseDTO toResponse(Product entity);


}
