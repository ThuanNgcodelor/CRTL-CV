package com.example.jobservice.request.job;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobUpdateRequest {
    @NotBlank(message = "Job ID cannot be blank")
    private String id;
    @NotBlank(message = "Job name cannot be blank")
    private String name;
    private String description;
    @NotBlank(message = "Category ID cannot be blank")
    private String categoryId;
    private String[] keys;
}
