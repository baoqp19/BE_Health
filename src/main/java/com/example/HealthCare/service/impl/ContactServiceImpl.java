package com.example.HealthCare.service.impl;

import com.example.HealthCare.dto.response.ContactResponse;
import com.example.HealthCare.model.Contact;
import com.example.HealthCare.repository.ContactRepository;
import com.example.HealthCare.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service

public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Page<ContactResponse> getAllContacts(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if(keyword != null && !keyword.isEmpty()) {
            return this.contactRepository.findByKeyword(keyword, pageable);
        }
        return this.contactRepository.findAllContacts(pageable);
    }

    @Override
    public Contact updateSeenStateUser(Integer id) {
        Contact contact = this.contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact is not found!!!"));

        if (contact.isStatus()) return contact;
        contact.setStatus(true);
        return this.contactRepository.save(contact);
    }
}
