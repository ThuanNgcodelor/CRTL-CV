package com.example.userservice.dto;

import lombok.Data;

@Data
public class VetProfileDto {
    private String userId;
    private String specialization;
    private Integer yearsExperience;
    private String clinicAddress;
    private String bio;
    private String availableHoursJson; // giữ dạng chuỗi JSON
}