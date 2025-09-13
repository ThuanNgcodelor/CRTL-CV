package com.example.stockservice.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReplyCreateRequest {
    private String content; // required
}
