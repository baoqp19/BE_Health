package com.example.HealthCare.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "medical_records")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "doctor")
    private String doctor;

    @Column(name = "symptoms")
    private String symptoms;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "facility_name")
    private String facilityName;

}