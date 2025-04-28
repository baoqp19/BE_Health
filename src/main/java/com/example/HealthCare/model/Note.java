package com.example.HealthCare.model;


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
@Table(name="notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="title")
    private String title;

    @Column(name="content")
    private String content;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name="note_index")
    private Long noteIndex;

    @Version
    @Column(name = "version")
    private Long version;  // Trường version để hỗ trợ optimistic locking
}