package com.example.jobservice.service.job;

import com.example.jobservice.client.FileStorageClient;
import com.example.jobservice.exception.NotFoundException;
import com.example.jobservice.model.Category;
import com.example.jobservice.model.Job;
import com.example.jobservice.repository.JobRepository;
import com.example.jobservice.request.job.JobCreateRequest;
import com.example.jobservice.request.job.JobUpdateRequest;
import com.example.jobservice.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final FileStorageClient fileStorageClient;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Override
    public Job createJob(JobCreateRequest request, MultipartFile file) {
        Category category = categoryService.getCategoryById(request.getCategoryId());
        String imageId = null;
        if (file != null)
            imageId = fileStorageClient.uploadImageToFIleSystem(file).getBody();
        return jobRepository.save(Job.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .keys(Optional.of(List.of(request.getKeys()))
                        .orElse(new ArrayList<>()))
                .imageId(imageId)
                .build());
    }

    @Override
    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(String id) {
        return findJobById(id);
    }

    @Override
    public Job updateJob(JobUpdateRequest request, MultipartFile file) {
        Job toUpdate = findJobById(request.getId());
        modelMapper.map(request,toUpdate);

        if(file != null) {
            String imageId = fileStorageClient.uploadImageToFIleSystem(file).getBody();
            if (imageId != null) {
                fileStorageClient.deleteImageFromFileSystem(toUpdate.getImageId());
                toUpdate.setImageId(imageId);
            }
        }
        return  jobRepository.save(toUpdate);
    }

    @Override
    public void deleteJobById(String id) {
        jobRepository.deleteById(id);
    }

    @Override
    public List<Job> getJobsByCategoryId(String categoryId) {
        return jobRepository.getGobsByCategoryId(categoryId);
    }

    @Override
    public List<Job> getJobsThatFitYourNeeds(String needs) {
        String[] keys = needs.replaceAll("\"", "").split(" ");
        HashMap<String, Integer> map = new HashMap<>();
        Arrays.stream(keys).forEach(key -> jobRepository.getJobsByKeysContainsIgnoreCase(key)
                .forEach(job -> {
                    if (map.containsKey(job.getId())) {
                        int count = map.get(job.getId());
                        map.put(job.getId(), count + 1);
                    } else map.put(job.getId(), 1);
                }));
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> findJobById(entry.getKey()))
                .collect(Collectors.toList());
    }

    @Override
    public Job findJobById(String id) {
        return jobRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Job not found"));
    }
}
