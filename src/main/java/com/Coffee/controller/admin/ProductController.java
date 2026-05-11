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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/all")
    public ApiResponse<List<ProductResponseDTO>> AllProducts(){
        return ApiResponse.success("Products retrivied successfully",productService.getAllProduct());
        // HttpStatus.OK

    }

    //get products with pagenation
    @GetMapping("")
    public ApiResponse<Page<ProductResponseDTO>> AllProducts ( @RequestParam(defaultValue = "0") int pageNumber,
    @RequestParam(defaultValue = "10") int pageSize,
    @RequestParam(defaultValue = "createdAt") String sortBy,
    @RequestParam(defaultValue = "des") String sortDir){
        Page<ProductResponseDTO> productResponseDtoPage = productService
                .getAllProductWithPagination(pageNumber, pageSize, sortBy, sortDir);

        return ApiResponse.success("Products retrivied successfully",productResponseDtoPage);
        // HttpStatus.OK

    }

    // get one product
    @GetMapping(value = "{productId}")
    public ApiResponse<ProductResponseDTO> getProduct(@PathVariable Long productId) throws IOException {
        return ApiResponse.success("Get Product successfully", productService.getOneProduct(productId));
        //, HttpStatus.CREATED
    }
    // create product
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponseDTO> createProduct(@Valid @ModelAttribute ProductRequestDTO productRequestDTO) throws IOException {
        return ApiResponse.success("Product created successfully", productService.createProduct(productRequestDTO));
        //, HttpStatus.CREATED
    }
// update product
    @PutMapping(value = "{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponseDTO> updateProduct(@PathVariable Long productId, @Valid   @ModelAttribute ProductRequestDTO productRequestDTO) throws IOException {
        return ApiResponse.success("Product updated successfully", productService.updateProduct(productId,productRequestDTO));
        //, HttpStatus.CREATED
    }

    @DeleteMapping("{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable Long productId) throws IOException {
        productService.deleteProduct(productId);
        return ApiResponse.success("Product deleted successfully",null); // ,HttpStatus.OK
    }

}
