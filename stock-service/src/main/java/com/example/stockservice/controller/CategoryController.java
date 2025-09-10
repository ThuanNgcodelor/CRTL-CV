package com.example.stockservice.controller;


import com.example.stockservice.dto.CategoryDto;
import com.example.stockservice.request.category.CategoryCreateRequest;
import com.example.stockservice.request.category.CategoryUpdateRequest;
import com.example.stockservice.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/stock/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    //{
    //  "name": "Electronics",
    //  "description": "Devices, gadgets and accessories"
    //}
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(categoryService.createCategory(request), CategoryDto.class));
    }
    // (GET /v1/stock/category/getAll)
    @GetMapping("/getAll")
    ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll().stream()
                .map(category -> modelMapper.map(category, CategoryDto.class)).toList());
    }

    //GET {{baseURL}}/v1/stock/category/getCategoryById/12345
    @GetMapping("/getCategoryById/{id}")
    ResponseEntity<CategoryDto> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(categoryService.getCategoryById(id), CategoryDto.class));
    }

    //{
    //  "id": "12345",
    //  "name": "Updated Electronics",
    //  "description": "All kinds of electronic devices"
    //}
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<CategoryDto> updateCategoryById(@Valid @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(modelMapper.map(categoryService.updateCategory(request), CategoryDto.class));
    }


    // DELETE {{baseURL}}/v1/stock/category/deleteCategoryById/12345
    @DeleteMapping("/deleteCategoryById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deleteCategoryById(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
