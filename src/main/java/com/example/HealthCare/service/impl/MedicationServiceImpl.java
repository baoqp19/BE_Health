package com.example.HealthCare.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.HealthCare.model.Medication;
import com.example.HealthCare.repository.MedicationRepository;
import com.example.HealthCare.service.MedicationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Medication addMedication(Medication medication) {
        return this.medicationRepository.save(medication);
    }

    @Override
    public Medication updateMedication(Medication medication) {
        Medication check = medicationRepository.findById(medication.getId())

                .orElseThrow(() -> new IllegalArgumentException("Medication not found"));
        medication.setId(check.getId());
        return this.medicationRepository.save(medication);
    }

    @Override
    public void deleteMedication(Integer medicationID) {
        this.medicationRepository.deleteById(medicationID);
    }

    @Override
    public Medication getMedicationById(Integer medicationID) {
        return this.medicationRepository.findById(medicationID)
                .orElseThrow(() -> new IllegalArgumentException("Medication not found"));
    }

    @Override
    public Page<Medication> getAllMedications(int page, int size, String keyword, Integer userID) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return this.medicationRepository.findByKeyword(keyword, pageable, userID);
        }
        return this.medicationRepository.getAllByUserID(pageable, userID);
    }
}
