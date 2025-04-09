package com.example.HealthCare.controller;


import com.example.HealthCare.Util.SecurityUtil;
import com.example.HealthCare.model.MedicalRecord;
import com.example.HealthCare.model.User;
import com.example.HealthCare.dto.request.medicalRecord.AddMedicalRecordRequest;
import com.example.HealthCare.dto.request.medicalRecord.UpdateMedicalRecordRequest;
import com.example.HealthCare.service.MedicalRecordService;
import com.example.HealthCare.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final UserService userService;
    public MedicalRecordController(MedicalRecordService medicalRecordService, UserService userService) {
        this.medicalRecordService = medicalRecordService;
        this.userService = userService;
    }

    @PostMapping("/medical-records")

    public ResponseEntity<MedicalRecord> addMedicalRecord(@Valid @RequestBody AddMedicalRecordRequest addMedicalRecordRequest) {

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .memberID(addMedicalRecordRequest.getMemberID())
                .date(addMedicalRecordRequest.getDate())
                .doctor(addMedicalRecordRequest.getDoctor())
                .symptoms(addMedicalRecordRequest.getSymptoms())
                .diagnosis(addMedicalRecordRequest.getDiagnosis())
                .treatment(addMedicalRecordRequest.getTreatment())
                .facilityName(addMedicalRecordRequest.getFacilityName())
                .build();
        log.info(medicalRecord.toString());
        MedicalRecord createdMedicalRecord = this.medicalRecordService.addMedicalRecord(medicalRecord);

        return new ResponseEntity<>(createdMedicalRecord, HttpStatus.OK);
    }

    @PutMapping("/medical-records/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateMedicalRecordRequest updateMedicalRecordRequest) {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .recordID(id)
                .memberID(updateMedicalRecordRequest.getMemberID())
                .date(updateMedicalRecordRequest.getDate())
                .doctor(updateMedicalRecordRequest.getDoctor())
                .symptoms(updateMedicalRecordRequest.getSymptoms())
                .diagnosis(updateMedicalRecordRequest.getDiagnosis())
                .treatment(updateMedicalRecordRequest.getTreatment())
                .facilityName(updateMedicalRecordRequest.getFacilityName())
                .build();
        log.info(medicalRecord.toString());
        MedicalRecord updatedMedicalRecord = this.medicalRecordService.updateMedicalRecord(medicalRecord);

        return new ResponseEntity<>(updatedMedicalRecord, HttpStatus.OK);
    }
    @DeleteMapping("/medical-records/{id}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable("id") Integer id) {
        this.medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.ok().body("xoá thành công");
    }
    @GetMapping("/medical-records/{id}")
    public ResponseEntity<Optional<MedicalRecord> > getMedicalRecordById(@PathVariable("id") Integer id) {
        Optional<MedicalRecord> medicalRecord = this.medicalRecordService.findMedicalRecordById(id);

        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @GetMapping("/medical-records")
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {

        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User user = this.userService.handleGetUserByEmail(email);

        Page<MedicalRecord> medicalRecordsPage = this.medicalRecordService.getAllMedicalRecords(page,size,keyword,user.getId());
        List<MedicalRecord> medicalRecords = medicalRecordsPage.getContent();


        return new ResponseEntity<>(medicalRecords, HttpStatus.OK);
    }
}
