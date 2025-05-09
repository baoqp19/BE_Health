package com.example.HealthCare.repository;

import com.example.HealthCare.dto.response.ContactResponse;
import com.example.HealthCare.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    @Query("SELECT new com.example.HealthCare.dto.response.ContactResponse(c.id, c.user.id, u.email, c.content, c.date, c.status) " +
            "FROM Contact c JOIN User u ON c.user.id = u.id " +
            "WHERE LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY c.date DESC")
    Page<ContactResponse> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new com.example.HealthCare.dto.response.ContactResponse(c.id, c.user.id, u.email, c.content, c.date, c.status) " +
            "FROM Contact c JOIN User u ON c.user.id = u.id " +
            "ORDER BY c.date DESC")
    Page<ContactResponse> findAllContacts(Pageable pageable);

}