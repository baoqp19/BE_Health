package com.example.HealthCare.mapper;

import com.example.HealthCare.dto.MedicalRecordResponse;
import com.example.HealthCare.model.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    MedicalRecordMapper INSTANCE = Mappers.getMapper( MedicalRecordMapper.class );

    MedicalRecordResponse toMedicalRecordResponse(MedicalRecord medicalRecord);

    default Page<MedicalRecordResponse> toMedicalRecordsResponse(Page<MedicalRecord> medicalRecords){
        return medicalRecords.map(this::toMedicalRecordResponse);
    };
}