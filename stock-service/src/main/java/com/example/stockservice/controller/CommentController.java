package com.example.stockservice.controller;

import com.example.stockservice.jwt.JwtUtil;
import com.example.stockservice.request.CommentCreateRequest;
import com.example.stockservice.request.ReplyCreateRequest;
import com.example.stockservice.respone.CommentResponse;
import com.example.stockservice.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/stock")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @GetMapping("/product/{productId}/comments")
    public ResponseEntity<List<CommentResponse>> list(@PathVariable String productId) {
        return ResponseEntity.ok(commentService.listForProduct(productId));
    }

    @PostMapping("/product/{productId}/comments")
    public ResponseEntity<CommentResponse> add(
            HttpServletRequest request,
            @PathVariable String productId,
            @RequestBody CommentCreateRequest req) {
        String userId = jwtUtil.ExtractUserId(request);
        return ResponseEntity.ok(commentService.addComment(userId, productId, req));
    }

    @PostMapping("/product/{productId}/comments/{commentId}/reply")
    public ResponseEntity<CommentResponse> reply(
            HttpServletRequest request,
            @PathVariable String productId,
            @PathVariable String commentId,
            @RequestBody ReplyCreateRequest req) {
        String userId = jwtUtil.ExtractUserId(request);
        return ResponseEntity.ok(commentService.addReply(userId, productId, commentId, req));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(
            HttpServletRequest request,
            @PathVariable String commentId) {
        String userId = jwtUtil.ExtractUserId(request);
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }
}
