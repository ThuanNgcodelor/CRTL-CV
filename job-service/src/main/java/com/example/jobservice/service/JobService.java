package com.example.jobservice.service;

import com.example.jobservice.model.Job;
import com.example.jobservice.request.job.JobCreateRequest;
import com.example.jobservice.request.job.JobUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobService {
    Job createJob(JobCreateRequest request, MultipartFile file);
    List<Job> getAll();
    Job getJobById(String id);
    Job updateJob(JobUpdateRequest request,MultipartFile file);
    void deleteJobById(String id);
    List<Job> getJobsByCategoryId(String categoryId);
    List<Job> getJobsThatFitYourNeeds(String needs);
    Job findJobById(String id);
}
