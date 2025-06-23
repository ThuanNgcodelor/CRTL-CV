package com.example.jobservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Entity(name = "jobs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Job extends BaseEntity {
    private String name;
    private String description;
    private String imageId;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Advert> adverts;

    @ElementCollection
    @CollectionTable(name = "job_keywords", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "keyword")
    private List<String> keys = Collections.emptyList();
}
