package com.example.HealthCare.controller;

import java.util.List;

import com.example.HealthCare.Util.SercurityUtil;
import com.example.HealthCare.model.User;
import com.example.HealthCare.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.HealthCare.model.Medication;
import com.example.HealthCare.dto.request.medication.AddMedicationRequest;
import com.example.HealthCare.dto.request.medication.UpdateMedicationRequest;
import com.example.HealthCare.service.MedicationService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class MedicationController {

    private final MedicationService medicationService;
    private final UserService userService;

    public MedicationController(MedicationService medicationService, UserService userService) {
        this.medicationService = medicationService;
        this.userService = userService;
    }

    @PostMapping("/medications")
    public ResponseEntity<?> addMedication(@Valid @RequestBody AddMedicationRequest addMedicationRequest) {
        Medication medication = Medication.builder()
                .recordID(addMedicationRequest.getRecordID())
                .name(addMedicationRequest.getName())
                .frequency(addMedicationRequest.getFrequency())
                .startDate(addMedicationRequest.getStartDate())
                .endDate(addMedicationRequest.getEndDate())
                .note(addMedicationRequest.getNote())
                .build();

        Medication createMediction = this.medicationService.addMedication(medication);

        return new ResponseEntity<>(createMediction, HttpStatus.CREATED);

    }

    @PutMapping("/medications/{id}")
    public ResponseEntity<?> updateMedication(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateMedicationRequest updateMedicationRequest) {
        Medication medication = Medication.builder()
                .medicationID(id)
                .name(updateMedicationRequest.getName())
                .frequency(updateMedicationRequest.getFrequency())
                .startDate(updateMedicationRequest.getStartDate())
                .endDate(updateMedicationRequest.getEndDate())
                .note(updateMedicationRequest.getNote())
                .build();
        Medication updatedMedication = this.medicationService.updateMedication(medication);
        
        return new ResponseEntity<>(updatedMedication, HttpStatus.OK);
    }

    @DeleteMapping("/medications/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable("id") Integer id) {
        this.medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medications/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable("id") Integer id) {
        Medication medication = this.medicationService.getMedicationById(id);

        return new ResponseEntity<>("xoá thành công", HttpStatus.OK);
    }

    @GetMapping("/medications")
    public ResponseEntity<List<Medication>> getAllMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {

        String email = SercurityUtil.getCurrentUserLogin().isPresent() ? SercurityUtil.getCurrentUserLogin().get() : "";

        User user = this.userService.handleGetUserByEmail(email);

        Page<Medication> medicationsPage = this.medicationService.getAllMedications(page, size, keyword, user.getId());

        List<Medication> medicationsContent = medicationsPage.getContent();

        return new ResponseEntity<>(medicationsContent, HttpStatus.OK);
    }

}
