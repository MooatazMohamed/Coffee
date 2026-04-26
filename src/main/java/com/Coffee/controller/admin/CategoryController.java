package com.Coffee.controller.admin;

import com.Coffee.dto.admin.request.CategoryRequestDTO;
import com.Coffee.dto.admin.response.CategoryResponseDTO;
import com.Coffee.model.Category;
import com.Coffee.service.admin.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private  CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO request) {
        CategoryResponseDTO savedCategory = categoryService.createCategoryWithAddons(request);
        return ResponseEntity.ok(savedCategory);
    }

}
