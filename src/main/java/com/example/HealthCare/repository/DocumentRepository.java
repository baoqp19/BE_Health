package com.example.HealthCare.repository;

import com.example.HealthCare.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    @Query("SELECT d FROM Document d WHERE LOWER(d.fileName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Document> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
