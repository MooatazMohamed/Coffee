package com.Coffee.controller.admin;

import com.Coffee.dto.admin.request.BranchRequestDTO;
import com.Coffee.dto.admin.request.ProductRequestDTO;
import com.Coffee.dto.admin.response.BranchResponseDTO;
import com.Coffee.dto.admin.response.ProductResponseDTO;
import com.Coffee.handler.ApiResponse;
import com.Coffee.model.Branch;
import com.Coffee.service.admin.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/product")
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {
    @Autowired
    ProductService productService ;

    @GetMapping("")
    public ApiResponse<List<ProductResponseDTO>> AllBranches(){
        return ApiResponse.success("Products retrivied successfully",productService.getAllProduct());
        // HttpStatus.OK

    }
    // get one product
    @GetMapping(value = "{productId}")
    public ApiResponse<ProductResponseDTO> getProduct(@PathVariable Long productId) throws IOException {
        return ApiResponse.success("Get Product successfully", productService.getOneProduct(productId));
        //, HttpStatus.CREATED
    }
    // create product
    @PostMapping
    public ApiResponse<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) throws IOException {
        return ApiResponse.success("Product created successfully", productService.createProduct(productRequestDTO));
        //, HttpStatus.CREATED
    }
// update product
    @PutMapping(value = "{productId}")
    public ApiResponse<ProductResponseDTO> updateProduct(@PathVariable Long productId, @Valid  @RequestBody ProductRequestDTO productRequestDTO) throws IOException {
        return ApiResponse.success("Product updated successfully", productService.updateProduct(productId,productRequestDTO));
        //, HttpStatus.CREATED
    }

    @DeleteMapping("{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable Long productId) throws IOException {
        productService.deleteProduct(productId);
        return ApiResponse.success("Product deleted successfully",null); // ,HttpStatus.OK
    }

}
