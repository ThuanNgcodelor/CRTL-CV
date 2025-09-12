package com.example.userservice.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdoptionReviewRequest {
    private Boolean approve;
    private String note;
}
