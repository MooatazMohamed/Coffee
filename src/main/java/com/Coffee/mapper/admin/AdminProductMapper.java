package com.Coffee.mapper.admin;

import com.Coffee.dto.admin.request.ProductRequestDTO;
import com.Coffee.dto.admin.response.ProductResponseDTO;
import com.Coffee.model.Product;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring" )
public interface AdminProductMapper {

    Product productDtoToEntity(ProductRequestDTO dto);
    ProductResponseDTO toResponse(Product entity);

}
