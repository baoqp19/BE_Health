package com.example.HealthCare.repository;

import com.example.HealthCare.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {

}
