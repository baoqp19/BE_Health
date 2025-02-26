package com.example.HealthCare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.HealthCare.dto.StudentLocation;
import com.example.HealthCare.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT new com.example.HealthCare.dto.StudentLocation(s.id, s.address, s.city, s.state, s.zip, s.country) FROM Student s WHERE s.id = ?1")

    Optional<StudentLocation> findStudentLocationById(Long id);
}
