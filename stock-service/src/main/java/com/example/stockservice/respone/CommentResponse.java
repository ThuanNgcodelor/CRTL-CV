package com.example.stockservice.respone;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentResponse {
    private String id;
    private String productId;
    private String userId;
    private String content;
    private Integer rating;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    @Builder.Default
    private List<CommentResponse> replies = new ArrayList<>();
}
