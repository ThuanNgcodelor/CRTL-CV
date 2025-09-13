package com.example.stockservice.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentCreateRequest {
    private String content;     // required
    private Integer rating;     // optional (only for top-level comment)
}
