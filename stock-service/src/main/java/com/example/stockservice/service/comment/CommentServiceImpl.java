package com.example.stockservice.service.comment;

import com.example.stockservice.model.Comment;
import com.example.stockservice.model.Product;
import com.example.stockservice.repository.CommentRepository;
import com.example.stockservice.repository.ProductRepository;
import com.example.stockservice.request.CommentCreateRequest;
import com.example.stockservice.request.ReplyCreateRequest;
import com.example.stockservice.respone.CommentResponse;
import com.example.stockservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepo;
    private final ProductRepository productRepo;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> listForProduct(String productId) {
        List<Comment> roots = commentRepo.findByProduct_IdAndParentIsNullOrderByCreatedAtDesc(productId);
        List<CommentResponse> out = new ArrayList<>();
        for (Comment c : roots) {
            out.add(toResponseTree(c));
        }
        return out;
    }

    @Override
    @Transactional
    public CommentResponse addComment(String userId, String productId, CommentCreateRequest req) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productId));

        if (req.getContent() == null || req.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content is required");
        }

        // NEW: validate rating if present
        if (req.getRating() != null) {
            int r = req.getRating();
            if (r < 1 || r > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5.");
            }
        }

        Comment c = Comment.builder()
                .product(product)
                .userId(userId)
                .content(req.getContent().trim())
                .rating(req.getRating())
                .build();
        Comment saved = commentRepo.save(c);
        return toResponseTree(saved);
    }

    @Override
    @Transactional
    public CommentResponse addReply(String userId, String productId, String parentCommentId, ReplyCreateRequest req) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productId));

        Comment parent = commentRepo.findById(parentCommentId)
                .orElseThrow(() -> new NoSuchElementException("Parent comment not found: " + parentCommentId));

        if (!parent.getProduct().getId().equals(product.getId())) {
            throw new IllegalArgumentException("Parent comment does not belong to product");
        }
        if (req.getContent() == null || req.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content is required");
        }

        // NEW: disallow replying to your own comment
        if (parent.getUserId() != null && parent.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You cannot reply to your own comment.");
        }

        // NEW: only allow one level of replies (no replying to a reply)
        if (parent.getParent() != null) {
            throw new IllegalArgumentException("Cannot reply to a reply (one level only).");
        }

        Comment reply = Comment.builder()
                .product(product)
                .parent(parent)
                .userId(userId)
                .content(req.getContent().trim())
                .build();
        Comment saved = commentRepo.save(reply);
        return toResponseTree(saved);
    }

    @Override
    @Transactional
    public void deleteComment(String userId, String commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found: " + commentId));
        if (!c.getUserId().equals(userId)) {
            // In a real app, check ADMIN role via SecurityContext. For now, owner-only delete.
            throw new SecurityException("You can only delete your own comment.");
        }
        commentRepo.delete(c);
    }

    private CommentResponse toResponseTree(Comment c) {
        CommentResponse res = CommentResponse.builder()
                .id(c.getId())
                .productId(c.getProduct().getId())
                .userId(c.getUserId())
                .content(c.getContent())
                .rating(c.getRating())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
        // children -> replies
        if (c.getChildren() != null && !c.getChildren().isEmpty()) {
            List<CommentResponse> replies = c.getChildren().stream()
                    .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                    .map(this::toResponseTree)
                    .collect(Collectors.toList());
            res.setReplies(replies);
        }
        return res;
    }
}
