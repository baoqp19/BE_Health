package com.example.HealthCare.service.impl;

import com.example.HealthCare.model.MedicalRecord;
import com.example.HealthCare.repository.MedicalRecordRepository;
import com.example.HealthCare.service.MedicalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        return this.medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
       MedicalRecord check = this.medicalRecordRepository.findById(medicalRecord.getId())
               .orElseThrow(() -> new IllegalArgumentException("Medical Record Not Found"));

        return this.medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public void deleteMedicalRecord(Integer medicalRecordID) {
        MedicalRecord check = this.medicalRecordRepository.findById(medicalRecordID)
                .orElseThrow(() -> new IllegalArgumentException("Medical Record not found"));
        this.medicalRecordRepository.deleteById(check.getId());
    }

    @Override
    public Page<MedicalRecord> getAllMedicalRecords(int page, int size, String keyword, Integer userID) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return this.medicalRecordRepository.findByKeyword(keyword, userID,pageable);
        }
        return this.medicalRecordRepository.findAllByUserID(pageable,userID);
    }

    @Override
    public Optional<MedicalRecord> findMedicalRecordById(Integer medicalRecordID) {
        return this.medicalRecordRepository.findById(medicalRecordID);
    }
}
