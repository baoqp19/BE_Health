package com.example.HealthCare.service.impl;

import com.example.HealthCare.model.Vaccination;
import com.example.HealthCare.repository.VaccicationRepository;
import com.example.HealthCare.service.VaccinationService;
import lombok.RequiredArgsConstructor;
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
    public Vaccination addVaccication(Vaccination vaccination) {
        return this.vaccicationRepository.save(vaccination);
    }

    @Override
    public Vaccination updateVaccication(Vaccination vaccination) {
        Vaccination check = this.vaccicationRepository.findById(vaccination.getVaccinationID())
                .orElseThrow(() -> new IllegalArgumentException("Vaccication not found"));
        vaccination.setVaccinationID(check.getVaccinationID());
        return this.vaccicationRepository.save(vaccination);
    }

    @Override
    public void deleteVaccication(Integer vaccicationID) {
        this.vaccicationRepository.deleteById(vaccicationID);
    }

    @Override
    public Vaccination getVaccicationById(Integer vaccicationID) {
        return this.vaccicationRepository.findById(vaccicationID)
                .orElseThrow(() -> new IllegalArgumentException("Vaccication not found"));
    }

    @Override
    public Page<Vaccination> getAllVaccications(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return this.vaccicationRepository.findByKeyword(keyword, pageable);
        }
        return this.vaccicationRepository.findAll(pageable);
    }

}
