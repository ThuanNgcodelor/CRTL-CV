package com.example.jobservice.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCreateRequest {
    @NotBlank(message = "Category name cannot be blank")
    private String name;
    private String description;
}
