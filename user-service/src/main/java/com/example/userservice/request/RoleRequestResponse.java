package com.example.userservice.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequestResponse {
    private String id;
    private String userId;
    private String requestedRole;
    private String reason;
    private String status;
}