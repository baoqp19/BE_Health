package com.example.HealthCare.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.HealthCare.model.Allergy;
import com.example.HealthCare.repository.AllergyRepository;
import com.example.HealthCare.service.AllergyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;

    public AllergyServiceImpl(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    @Override
    public Allergy addAllergy(Allergy allergy) {
        return this.allergyRepository.save(allergy);
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
    public Page<Allergy> getAllAllergies(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return this.allergyRepository.findByKeyword(keyword, pageable);
        }
        return this.allergyRepository.findAll(pageable);
    }

    @Override
    public Optional<Allergy> findAllergyById(Integer allergyID) {
        return allergyRepository.findById(allergyID);
    }

}
