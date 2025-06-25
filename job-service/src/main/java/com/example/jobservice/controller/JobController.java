package com.example.jobservice.controller;

import com.example.jobservice.dto.JobDto;
import com.example.jobservice.request.job.JobCreateRequest;
import com.example.jobservice.request.job.JobUpdateRequest;
import com.example.jobservice.service.job.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/job/job")
public class JobController {
    private final JobService jobService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDto> createJob(@Valid @RequestPart JobCreateRequest request,
                                            @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(jobService.createJob(request, file), JobDto.class));
    }

    @PostMapping("/getJobsThatFitYourNeeds/{needs}")
    public ResponseEntity<List<JobDto>> getJobsThatFitYourNeeds(@PathVariable String needs){
        return ResponseEntity.ok(jobService.getJobsThatFitYourNeeds(needs)
                .stream().map(job -> modelMapper.map(job, JobDto.class)).toList());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDto> updateJob(@Valid @RequestPart JobUpdateRequest request,
                                            @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.ok(modelMapper.map(jobService.updateJob(request, file), JobDto.class));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<JobDto>> getAllJobs(){
        return ResponseEntity.ok(jobService.getAll().stream()
                .map(map-> modelMapper.map(map, JobDto.class)).toList());
    }

    @GetMapping("/getJobById/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable String id){
        return ResponseEntity.ok(modelMapper.map(jobService.getJobById(id), JobDto.class));
    }

    @GetMapping("/getJobsByCategoryId/{categoryId}")
    public ResponseEntity<List<JobDto>> getJobsByCategoryId(@PathVariable String categoryId){
        return ResponseEntity.ok(jobService.getJobsByCategoryId(categoryId)
                .stream().map(map -> modelMapper.map(map, JobDto.class)).toList());
    }

    @DeleteMapping("/deleteJobById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deleteJobById(@PathVariable String id) {
        jobService.deleteJobById(id);
        return ResponseEntity.ok().build();
    }

}
