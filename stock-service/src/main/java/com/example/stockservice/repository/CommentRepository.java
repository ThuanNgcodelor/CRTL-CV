package com.example.stockservice.repository;

import com.example.stockservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByProduct_IdAndParentIsNullOrderByCreatedAtDesc(String productId);
    List<Comment> findByParent_IdOrderByCreatedAtAsc(String parentId);
}
