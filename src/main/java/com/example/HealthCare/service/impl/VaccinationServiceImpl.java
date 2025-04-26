package com.example.HealthCare.service.impl;

import com.example.HealthCare.model.Vaccication;
import com.example.HealthCare.repository.VaccicationRepository;
import com.example.HealthCare.service.VaccinationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VaccinationServiceImpl implements VaccinationService {

    private final VaccicationRepository vaccicationRepository;

    public VaccinationServiceImpl(VaccicationRepository vaccicationRepository) {
        this.vaccicationRepository = vaccicationRepository;
    }

    @Override
    public Vaccication addVaccication(Vaccication vaccination) {
        return this.vaccicationRepository.save(vaccination);
    }

    @Override
    public Vaccication updateVaccication(Vaccication vaccination) {
        Vaccication check = this.vaccicationRepository.findById(vaccination.getVaccinationID())
                .orElseThrow(() -> new IllegalArgumentException("Vaccication not found"));
        vaccination.setVaccinationID(check.getVaccinationID());
        return this.vaccicationRepository.save(vaccination);
    }

    @Override
    public void deleteVaccication(Integer vaccicationID) {
        this.vaccicationRepository.deleteById(vaccicationID);
    }

    @Override
    public Vaccication getVaccicationById(Integer vaccicationID) {
        return this.vaccicationRepository.findById(vaccicationID)
                .orElseThrow(() -> new IllegalArgumentException("Vaccication not found"));
    }

    @Override
    public Page<Vaccication> getAllVaccications(int page, int size, int userId, String keyword, Long memberId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (memberId != null) {
            return vaccicationRepository.findByKeywordAndMember(memberId, keyword, userId, pageable);
        } else {
            return vaccicationRepository.findByKeyword(keyword, userId, pageable);
        }
    }

}
