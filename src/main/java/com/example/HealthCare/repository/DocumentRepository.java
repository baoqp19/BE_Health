package com.example.HealthCare.repository;

import com.example.HealthCare.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
