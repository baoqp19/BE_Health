package com.example.HealthCare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.HealthCare.model.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    @Query("SELECT m FROM Medication m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Medication> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
