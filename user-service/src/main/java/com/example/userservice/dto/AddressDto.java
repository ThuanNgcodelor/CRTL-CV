package com.example.userservice.dto;

import lombok.Data;

@Data
public class AddressDto {
    public String userId;
    public String recipientName;
    public String recipientPhone;
    public String province;
    public String streetAddress;
    public Boolean isDefault;
}
