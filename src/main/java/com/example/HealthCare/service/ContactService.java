package com.example.HealthCare.service;

import com.example.HealthCare.dto.response.ContactResponse;
import com.example.HealthCare.model.Contact;
import org.springframework.data.domain.Page;

public interface ContactService {
    Page<ContactResponse> getAllContacts(int page, int size, String keyword);
    Contact updateSeenStateUser(Integer id);
}
