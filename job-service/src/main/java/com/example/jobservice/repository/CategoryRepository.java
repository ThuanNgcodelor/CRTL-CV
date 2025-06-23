package com.example.jobservice.repository;

import com.example.jobservice.model.Category;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Tag(name = "Category Repository", description = "Repository for managing categories")
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}
