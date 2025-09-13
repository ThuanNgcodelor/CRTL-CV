package com.example.stockservice.service;

import com.example.stockservice.request.CommentCreateRequest;
import com.example.stockservice.request.ReplyCreateRequest;
import com.example.stockservice.respone.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> listForProduct(String productId);
    CommentResponse addComment(String userId, String productId, CommentCreateRequest req);
    CommentResponse addReply(String userId, String productId, String parentCommentId, ReplyCreateRequest req);
    void deleteComment(String userId, String commentId);
}
