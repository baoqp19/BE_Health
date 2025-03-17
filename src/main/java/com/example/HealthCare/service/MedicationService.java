package com.example.HealthCare.service;

import org.springframework.data.domain.Page;

import com.example.HealthCare.model.Medication;



public interface MedicationService {

    Medication addMedication(Medication medication);

    Medication updateMedication(Medication medication);

    void deleteMedication(Integer medicationID);

    Medication getMedicationById(Integer medicationID);

    Page<Medication> getAllMedications(int page, int size, String keyword, Integer userID);
}
