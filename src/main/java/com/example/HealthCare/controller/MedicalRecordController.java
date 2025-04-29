package com.example.HealthCare.controller;


import com.example.HealthCare.Util.CustomPagination;
import com.example.HealthCare.Util.SecurityUtil;
import com.example.HealthCare.dto.MedicalRecordResponse;
import com.example.HealthCare.dto.response.ApiResponse;
import com.example.HealthCare.model.MedicalRecord;
import com.example.HealthCare.model.Member;
import com.example.HealthCare.model.User;
import com.example.HealthCare.dto.request.medicalRecord.AddMedicalRecordRequest;
import com.example.HealthCare.dto.request.medicalRecord.UpdateMedicalRecordRequest;
import com.example.HealthCare.service.MedicalRecordService;
import com.example.HealthCare.service.MemberService;
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
    private final MemberService memberService;
    public MedicalRecordController(MedicalRecordService medicalRecordService, UserService userService, MemberService memberService) {
        this.medicalRecordService = medicalRecordService;
        this.userService = userService;
        this.memberService = memberService;
    }

    @PostMapping("/medical-records")
    public ResponseEntity<?> addMedicalRecord(
            @Valid @RequestBody AddMedicalRecordRequest addMedicalRecordRequest) {
        MedicalRecord medicalRecord = medicalRecordService.addMedicalRecord(addMedicalRecordRequest);
        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @PutMapping("/medical-records/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateMedicalRecordRequest updateMedicalRecordRequest) {

        Member member = memberService.getMemberById(updateMedicalRecordRequest.getMemberId());
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .id(id)
                .member(member)
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
    public ResponseEntity<Optional<?> > getMedicalRecordById(@PathVariable("id") Integer id) {
        Optional<MedicalRecord> medicalRecord = this.medicalRecordService.findMedicalRecordById(id);

        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @GetMapping("/medical-records")
    public ResponseEntity<?> getAllMedicalRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {

        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User user = this.userService.handleGetUserByEmail(email);

        CustomPagination<MedicalRecordResponse> medicalRecordsPage = medicalRecordService.getAllMedicalRecords(page,size,keyword,user.getId());

        return new ResponseEntity<>(medicalRecordsPage, HttpStatus.OK);
    }
}
