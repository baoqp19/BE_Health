package com.example.HealthCare.service;

import com.example.HealthCare.model.MedicalRecord;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MedicalRecordService {
    //CRUD
    MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);
    MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);
    void deleteMedicalRecord(Integer medicalRecordID);
    Page<MedicalRecord> getAllMedicalRecords(int page, int size, String keyword, Integer userID);
    //Search
    Optional<MedicalRecord> findMedicalRecordById(Integer medicalRecordID);
}
