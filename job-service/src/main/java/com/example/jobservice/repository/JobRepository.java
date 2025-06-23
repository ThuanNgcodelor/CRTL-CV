package com.example.jobservice.repository;

import com.example.jobservice.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,String> {
    List<Job> getGobsByCategoryId(String categoryId);
    List<Job> getJobsByKeysContainsIgnoreCase(String key);
}
