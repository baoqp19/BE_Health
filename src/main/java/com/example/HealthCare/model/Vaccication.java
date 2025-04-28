package com.example.HealthCare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vaccications")
public class Vaccication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "vaccine_name", nullable = false)
    private String vaccineName;

    @Column(name = "date_administered", nullable = false)
    private Date dateAdministered;

}
