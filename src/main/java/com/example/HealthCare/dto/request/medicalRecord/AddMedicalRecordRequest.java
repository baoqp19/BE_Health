package com.example.HealthCare.dto.request.medicalRecord;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddMedicalRecordRequest {
    private Integer id;

    private Integer memberId;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Upload date must be a date in the past or present")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.time.LocalDate date;

    @NotBlank(message = "Doctor's name is required")
    @Size(max = 100, message = "Doctor's name must not exceed 100 characters")
    private String doctor;

    @NotBlank(message = "Symptoms is required")
    @Size(max = 300, message = "Symptoms must not exceed 100 characters")
    private String symptoms;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 200, message = "Diagnosis must not exceed 200 characters")
    private String diagnosis;

    @NotBlank(message = "Treatment is required")
    @Size(max = 200, message = "Treatment must not exceed 200 characters")
    private String treatment;

    @NotBlank(message = "Facility name is required")
    @Size(max = 50, message = "Facility name must not exceed 50 characters")
    private String facilityName;

    @NotNull(message = "Medications are required")
    private List<MedicationRequest> medications = new ArrayList<>();

    @NotNull(message = "Documents are required")
    private List<DocumentRequest> documents = new ArrayList<>();

}
