package com.example.jobservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity(name = "categories")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category extends BaseEntity {
    private String name;
    private String description;
    private String imageId;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Job> jobs;
}
