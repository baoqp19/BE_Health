package com.example.HealthCare.repository;

import com.example.HealthCare.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT m FROM Appointment m " + "WHERE LOWER(m.doctor) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    Page<Appointment> findByKeyword(@Param("keyword") String keyword,
                                    Pageable pageable);
}
