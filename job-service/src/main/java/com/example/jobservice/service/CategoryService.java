package com.example.jobservice.service;

import com.example.jobservice.model.Category;
import com.example.jobservice.request.category.CategoryCreateRequest;
import com.example.jobservice.request.category.CategoryUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryCreateRequest request, MultipartFile multipartFile);
    Category updateCategory(CategoryUpdateRequest request, MultipartFile multipartFile);
    List<Category> getAll();
    Category getCategoryById(String id);
    Category  findCategoryById(String id);
    void deleteCategory(String id);
}
