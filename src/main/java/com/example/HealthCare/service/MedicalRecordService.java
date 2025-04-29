package com.example.HealthCare.service;

import com.example.HealthCare.Util.CustomPagination;
import com.example.HealthCare.dto.MedicalRecordResponse;
import com.example.HealthCare.dto.request.medicalRecord.AddMedicalRecordRequest;
import com.example.HealthCare.model.MedicalRecord;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MedicalRecordService {
    //CRUD
    MedicalRecord addMedicalRecord(AddMedicalRecordRequest addMedicalRecordRequest);
    MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);
    void deleteMedicalRecord(Integer medicalRecordID);
    CustomPagination<MedicalRecordResponse> getAllMedicalRecords(int page, int size, String keyword, Integer userID);
    //Search
    Optional<MedicalRecord> findMedicalRecordById(Integer medicalRecordID);
}
