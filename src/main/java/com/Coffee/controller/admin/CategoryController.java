package com.Coffee.controller.admin;

import com.Coffee.dto.admin.request.BranchRequestDTO;
import com.Coffee.dto.admin.request.CategoryRequestDTO;
import com.Coffee.dto.admin.response.BranchResponseDTO;
import com.Coffee.dto.admin.response.CategoryResponseDTO;
import com.Coffee.handler.ApiResponse;
import com.Coffee.model.Category;
import com.Coffee.service.admin.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private  CategoryService categoryService;

    // get All category
    @GetMapping("")
    public ApiResponse<List<CategoryResponseDTO>> getAllCategory() {
        return ApiResponse.success("Get Categories successfully", categoryService.getAllCategoryWithAddons());
    }

    // get 1 category
    @GetMapping("{categoryId}")
    public ApiResponse<CategoryResponseDTO> getOneCategory(@PathVariable Long categoryId){

        return ApiResponse.success("Get Category successfully",categoryService.getCategoryWithAddons(categoryId)); // ,HttpStatus.OK
    }
    // create category
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CategoryResponseDTO>  createCategory(@Valid @ModelAttribute CategoryRequestDTO request) {
        CategoryResponseDTO savedCategory = categoryService.createCategoryWithAddons(request);
        return ApiResponse.success("Category Created successfully",savedCategory); // ,HttpStatus.OK



    }
    // update category
    @PutMapping(value = "{categoryId}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CategoryResponseDTO> updateCategory(@PathVariable Long categoryId, @Valid @ModelAttribute CategoryRequestDTO  categoryRequestDTO) throws IOException {
        return ApiResponse.success("Category updated successfully", categoryService.updateCategoryWithAddons(categoryId,categoryRequestDTO));
        //, HttpStatus.CREATED
    }

    // delete category
    @DeleteMapping("{categoryId}")
    public ApiResponse<String> deleteCategory(@PathVariable Long categoryId) throws IOException {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.success("Category deleted successfully",null); // ,HttpStatus.OK
    }
}
