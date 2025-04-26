package com.example.HealthCare.service.impl;

import java.util.Optional;

import com.example.HealthCare.Util.CustomPagination;
import com.example.HealthCare.dto.request.allergy.AddAllergyRequest;
import com.example.HealthCare.dto.response.AllergyResponse;
import com.example.HealthCare.mapper.AllergyMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.HealthCare.model.Allergy;
import com.example.HealthCare.repository.AllergyRepository;
import com.example.HealthCare.service.AllergyService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;


    public AllergyServiceImpl(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    @Override
    public Allergy addAllergy(AddAllergyRequest addAllergyRequest) {
        Allergy allergy = AllergyMapper.INSTANCE.convectToAllergy(addAllergyRequest);
        return allergyRepository.save(allergy);
    }

    @Override
    public Allergy updateAllergy(Allergy allergy) {
        Allergy check = this.allergyRepository.findById(allergy.getAllergyID())
                .orElseThrow(() -> new IllegalArgumentException("Allergy not found "));
        return this.allergyRepository.save(allergy);
    }

    @Override
    public void deleteAllergy(Integer allergyID) {
        Allergy check = allergyRepository.findById(allergyID)
                .orElseThrow(() -> new IllegalArgumentException("Allergy not found"));
        allergyRepository.deleteById(check.getAllergyID());
    }

    @Override
    public CustomPagination<AllergyResponse> getAllAllergies(int page, int size, String keyword, Integer userID, Long memberId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Allergy> allergies;
        if(memberId != null){
            allergies = allergyRepository.findByKeywordAndMember(memberId, keyword, userID, pageable);
        } else {
            allergies = allergyRepository.findByKeyword(keyword, pageable, userID);
        }
        Page<AllergyResponse> allergyResponses = allergies.map(AllergyMapper.INSTANCE::toAllergyResponse);
        CustomPagination<AllergyResponse> allergyContent = new CustomPagination<>(allergyResponses);
        return allergyContent;
    }

    @Override
    public Optional<Allergy> findAllergyById(Integer allergyID) {
        return allergyRepository.findById(allergyID);
    }

}
