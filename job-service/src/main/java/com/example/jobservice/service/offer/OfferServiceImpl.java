package com.example.jobservice.service.offer;

import com.example.jobservice.client.UserServiceClient;
import com.example.jobservice.dto.UserDto;
import com.example.jobservice.enums.OfferStatus;
import com.example.jobservice.exception.NotFoundException;
import com.example.jobservice.model.Advert;
import com.example.jobservice.model.Offer;
import com.example.jobservice.repository.OfferRepository;
import com.example.jobservice.request.notification.SendNotificationRequest;
import com.example.jobservice.request.offer.MakeAnOfferRequest;
import com.example.jobservice.request.offer.OfferUpdateRequest;
import com.example.jobservice.service.advert.AdvertService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final AdvertService advertService;
    private final UserServiceClient userServiceClient;
    private final ModelMapper modelMapper;
    private final NewTopic topic;
    private final KafkaTemplate<String, SendNotificationRequest> kafkaTemplate;

    @Override
    public Offer makeAnOffer(MakeAnOfferRequest request) {
        String userId = getUserById(request.getUserId()).getId();
        Advert advert = advertService.getAdvertById(request.getAdvertId());
        Offer toSave = Offer.builder()
                .userId(userId)
                .advert(advert)
                .offeredPrice(request.getOfferedPrice())
                .status(OfferStatus.OPEN)
                .build();
        offerRepository.save(toSave);

        SendNotificationRequest notification = SendNotificationRequest.builder()
                .message("You have received an offer for your advertising.")
                .userId(userId)
                .offerId(toSave.getId())
                .build();

        kafkaTemplate.send(topic.name(), notification);
        return toSave;
    }

    @Override
    public Offer getOfferById(String id) {
        return findOfferById(id);
    }

    @Override
    public List<Offer> getOffersByAdvertId(String advertId) {
        Advert advert = advertService.getAdvertById(advertId);
        return offerRepository.getOffersByAdvertId(advert.getId());
    }

    @Override
    public List<Offer> getOffersByUserId(String id) {
        String userId = getUserById(id).getId();
        return offerRepository.getOffersByUserId(userId);
    }

    @Override
    public UserDto getUserById(String userId) {
        return Optional.ofNullable(userServiceClient.getUserById(userId).getBody()).orElseThrow(()-> new NotFoundException("User not found"));
    }

    @Override
    public Offer updateOfferById(OfferUpdateRequest request) {
        Offer toUpdate = findOfferById(request.getId());
        modelMapper.map(request, toUpdate);
        return offerRepository.save(toUpdate);
    }

    @Override
    public void deleteOfferById(String id) {
        offerRepository.deleteById(id);
    }

    @Override
    public boolean authorizeCheck(String id, String principal) {
        return getUserById(getOfferById(id).getUserId()).getEmail().equals(principal);
    }

    @Override
    public Offer findOfferById(String id) {
        return offerRepository.findById(id).orElseThrow(()-> new NotFoundException("Offer not found"));
    }

}
