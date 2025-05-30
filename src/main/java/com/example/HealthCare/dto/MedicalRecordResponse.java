package com.example.HealthCare.dto;

import com.example.HealthCare.dto.response.DocumentDTO;
import com.example.HealthCare.dto.response.MedicationDTO;
import com.example.HealthCare.dto.response.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordResponse {
    private int id;
    private MemberDTO member;
    private LocalDate date;
    private String doctor;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private String facilityName;
    private List<MedicationDTO> medications;
    private List<DocumentDTO> documents;
}