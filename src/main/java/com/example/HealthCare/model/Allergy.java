package com.example.HealthCare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "allergies") 
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int allergyID;

    @Column(name = "member_id", nullable = false)
    private int memberID;

    @Column(name = "allergy_type")
    private String allergyType;

    @Column(name = "severity")
    private String severity;

    @Column(name = "symptoms")
    private String symptoms;


}
