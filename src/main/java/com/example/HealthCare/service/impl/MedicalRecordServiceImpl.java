package com.example.HealthCare.service.impl;

import com.example.HealthCare.Util.CustomPagination;
import com.example.HealthCare.dto.MedicalRecordResponse;
import com.example.HealthCare.dto.request.medicalRecord.AddMedicalRecordRequest;
import com.example.HealthCare.dto.request.medicalRecord.DocumentRequest;
import com.example.HealthCare.dto.request.medicalRecord.MedicationRequest;
import com.example.HealthCare.mapper.MedicalRecordMapper;
import com.example.HealthCare.model.Document;
import com.example.HealthCare.model.MedicalRecord;
import com.example.HealthCare.model.Medication;
import com.example.HealthCare.model.Member;
import com.example.HealthCare.repository.DocumentRepository;
import com.example.HealthCare.repository.MedicalRecordRepository;
import com.example.HealthCare.repository.MedicationRepository;
import com.example.HealthCare.service.CloudinaryService;
import com.example.HealthCare.service.MedicalRecordService;
import com.example.HealthCare.service.MemberService;
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
    private final MedicationRepository medicationRepository;
    private final DocumentRepository documentRepository;
    private final MemberService memberService;
    private final CloudinaryService cloudinaryService;

    public MedicalRecordServiceImpl(
            MedicalRecordRepository medicalRecordRepository,
            MedicationRepository medicationRepository,
            DocumentRepository documentRepository,
            MemberService memberService,
            CloudinaryService cloudinaryService

    ) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.cloudinaryService = cloudinaryService;
        this.memberService = memberService;
        this.documentRepository = documentRepository;
        this.medicationRepository = medicationRepository;
    }

    @Override
    public MedicalRecord addMedicalRecord(AddMedicalRecordRequest addMedicalRecordRequest) {
        Member member = memberService.getMemberById(addMedicalRecordRequest.getMemberId());
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .member(member)
                .date(addMedicalRecordRequest.getDate())
                .doctor(addMedicalRecordRequest.getDoctor())
                .symptoms(addMedicalRecordRequest.getSymptoms())
                .diagnosis(addMedicalRecordRequest.getDiagnosis())
                .treatment(addMedicalRecordRequest.getTreatment())
                .facilityName(addMedicalRecordRequest.getFacilityName())
                .build();
        medicalRecord = medicalRecordRepository.save(medicalRecord);
        for(MedicationRequest medicationRequest : addMedicalRecordRequest.getMedications()){
            Medication medication = Medication.builder()
                    .record(medicalRecord)
                    .name(medicationRequest.getName())
                    .startDate(medicationRequest.getStartDate())
                    .endDate(medicationRequest.getEndDate())
                    .position(medicationRequest.getPosition())
                    .frequency(medicationRequest.getFrequency())
                    .build();
            medicationRepository.save(medication);
        }
        for (DocumentRequest documentRequest : addMedicalRecordRequest.getDocuments()){
            Document document = Document.builder()
                    .record(medicalRecord)
                    .name(documentRequest.getName())
                    .position(documentRequest.getPosition())
                    .size(documentRequest.getSize())
                    .type(documentRequest.getType())
                    .path(documentRequest.getPath())
                    .build();
            documentRepository.save(document);
        }
        return medicalRecord;
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
    public CustomPagination<MedicalRecordResponse> getAllMedicalRecords(int page, int size, String keyword, Integer userID) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<MedicalRecord> medicalRecords =  medicalRecordRepository.findByKeyword(keyword, userID,pageable);
        Page<MedicalRecordResponse> medicalRecordResponses = MedicalRecordMapper.INSTANCE.toMedicalRecordsResponse(medicalRecords);
        CustomPagination<MedicalRecordResponse> customPagination = new CustomPagination<>(medicalRecordResponses);
        return customPagination;
    }

    @Override
    public Optional<MedicalRecord> findMedicalRecordById(Integer medicalRecordID) {
        return this.medicalRecordRepository.findById(medicalRecordID);
    }
}
