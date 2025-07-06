package com.example.jobservice.service.advert;

import com.example.jobservice.client.FileStorageClient;
import com.example.jobservice.client.UserServiceClient;
import com.example.jobservice.dto.UserDto;
import com.example.jobservice.enums.AdvertStatus;
import com.example.jobservice.enums.Advertiser;
import com.example.jobservice.exception.NotFoundException;
import com.example.jobservice.model.Advert;
import com.example.jobservice.model.Job;
import com.example.jobservice.repository.AdvertRepository;
import com.example.jobservice.request.advert.AdvertCreateRequest;
import com.example.jobservice.request.advert.AdvertUpdateRequest;
import com.example.jobservice.service.job.JobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvertServiceImpl implements AdvertService {
    private final AdvertRepository advertRepository;
    private final UserServiceClient userServiceClient;
    private final FileStorageClient fileStorageClient;
    private final JobService jobService;
    private final ModelMapper modelMapper;

    @Override
    public Advert createAdvert(AdvertCreateRequest request, MultipartFile file) {
        String userId = getUserById(request.getUserId()).getId();
        Job job = jobService.getJobById(request.getJobId());

        String imageId = null;

        if (file != null)
            imageId = fileStorageClient.uploadImageToFIleSystem(file).getBody();

        Advert toSave = Advert.builder()
                .userId(userId)
                .job(job)
                .name(request.getName())
                .advertiser(request.getAdvertiser())
                .deliveryTime(request.getDeliveryTime())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(AdvertStatus.OPEN)
                .imageId(imageId)
                .build();
        return advertRepository.save(toSave);
    }

    @Override
    public Advert updateAdvertById(AdvertUpdateRequest request, MultipartFile file) {
        Advert toUpdate = findAdvertById(request.getId());
        modelMapper.map(request,toUpdate);
        if (file != null){
            String imageId = fileStorageClient.uploadImageToFIleSystem(file).getBody();
            if (imageId != null) {
                fileStorageClient.deleteImageFromFileSystem(imageId);
                toUpdate.setImageId(imageId);
            }
        }
        return advertRepository.save(toUpdate);
    }

    @Override
    public List<Advert> getAll() {
        return advertRepository.findAll();
    }

    @Override
    public Advert getAdvertById(String id) {
        return findAdvertById(id);
    }

    protected Advert findAdvertById(String id) {
        return advertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advert not found with id: " + id));
    }

    @Override
    public void deleteAdvertById(String id) {
        advertRepository.deleteById(id);
    }

    @Override
    public List<Advert> getAdvertByUserId(String id, Advertiser advertiser) {
        String userId = getUserById(id).getId();
        return advertRepository.getAdvertsByUserIdAndAdvertiser(userId, advertiser);
    }

    @Override
    public UserDto getUserById(String userId) {
        return Optional.ofNullable(userServiceClient.getUserById(userId).getBody())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    public boolean authorizeCheck(String id, String principal){
        return getUserById(getAdvertById(id).getUserId()).getId().equals(principal);
    }
}
