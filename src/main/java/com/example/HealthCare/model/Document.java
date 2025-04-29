package com.example.HealthCare.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "position", nullable = false)
    private int position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private MedicalRecord record;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type",nullable = false)
    private String type;

    @Column(name = "path", nullable = false)
    private String path;
}
