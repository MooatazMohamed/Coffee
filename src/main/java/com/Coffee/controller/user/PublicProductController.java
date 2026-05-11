package com.Coffee.controller.user;

import com.Coffee.dto.admin.response.ProductResponseDTO;
import com.Coffee.handler.ApiResponse;
import com.Coffee.service.admin.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class PublicProductController {

    @Autowired
    ProductService productService;

    @GetMapping("")
    public ApiResponse<List<ProductResponseDTO>> getAllProducts() {

        return ApiResponse.success("Products retrieved successfully",productService.getAllProduct());
    }

    @GetMapping(value = "/{productId}")
    public ApiResponse<ProductResponseDTO> getProduct(@PathVariable Long productId) throws IOException {
        return ApiResponse.success("Get Product successfully", productService.getOneProduct(productId));
    }
}
