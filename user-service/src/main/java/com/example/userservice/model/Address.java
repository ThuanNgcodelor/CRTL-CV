package com.example.userservice.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Address extends BaseEntity {
    public String userId;
    public String addressName;
    public String recipientName;
    public String recipientPhone;
    public String province;
    public String streetAddress;
    public Boolean isDefault = false;
}
