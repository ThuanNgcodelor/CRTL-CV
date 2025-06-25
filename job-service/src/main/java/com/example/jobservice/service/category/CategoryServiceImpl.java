package com.example.jobservice.service.category;

import com.example.jobservice.client.FileStorageClient;
import com.example.jobservice.exception.NotFoundException;
import com.example.jobservice.model.Category;
import com.example.jobservice.repository.CategoryRepository;
import com.example.jobservice.request.category.CategoryCreateRequest;
import com.example.jobservice.request.category.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final FileStorageClient fileStorageClient;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    // Create a new category with an image
    @Override
    public Category createCategory(CategoryCreateRequest request, MultipartFile multipartFile) {
        String imageId = null;
        if(multipartFile != null)
            imageId = fileStorageClient.uploadImageToFIleSystem(multipartFile).getBody();

        return categoryRepository.save(
                Category.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .imageId(imageId)
                        .build()
        );
    }
    // Create a new category with an image


    @Override
    public Category updateCategory(CategoryUpdateRequest request, MultipartFile multipartFile) {
        Category toUpdate = findCategoryById(request.getId());
        modelMapper.map(request,toUpdate);
        if(multipartFile != null){
            String imageId = fileStorageClient.uploadImageToFIleSystem(multipartFile).getBody();
            if (imageId != null) {
                fileStorageClient.deleteImageFromFileSystem(imageId);
                toUpdate.setImageId(imageId);
            }
        }
        return toUpdate;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(String id) {
        return findCategoryById(id);
    }

    @Override
    public Category findCategoryById(String id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category not found")
        );
    }

    @Override
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
}
