package com.example.HealthCare.service.impl;

import com.example.HealthCare.model.EmergencyContact;
import com.example.HealthCare.repository.EmergencyContactRepository;
import com.example.HealthCare.service.EmergencyContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmergencyContactServiceImpl implements EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;

    public EmergencyContactServiceImpl(EmergencyContactRepository emergencyContactRepository) {
        this.emergencyContactRepository = emergencyContactRepository;
    }


    public EmergencyContact save(EmergencyContact emergencyContact) {

        // Kiểm tra ràng buộc nghiệp vụ (nếu cần)
        if (emergencyContact.getName() == null || emergencyContact.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Name and Phone Number cannot be null");
        }

        return this.emergencyContactRepository.save(emergencyContact);
    }

    @Override
    public EmergencyContact addEmergencyContact(EmergencyContact emergencyContact) {
        return this.emergencyContactRepository.save(emergencyContact);
    }

    @Override
    public EmergencyContact updateEmergencyContact(EmergencyContact emergencyContact) {

        EmergencyContact check = emergencyContactRepository.findById(emergencyContact.getId())
                .orElseThrow(() -> new IllegalArgumentException("Emergency contact not found"));
        emergencyContact.setUser(check.getUser());

        return emergencyContactRepository.save(emergencyContact);
    }

    @Override
    public void deleteEmergencyContact(Integer contactID) {
        this.emergencyContactRepository.deleteById(contactID);
    }

    @Override
    public EmergencyContact getEmergencyContactById(Integer contactID) {
        return this.emergencyContactRepository.findById(contactID)
                .orElseThrow(() -> new IllegalArgumentException("Emergency contact not found"));
    }

    @Override
    public Page<EmergencyContact> getAllEmergencyContacts(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return emergencyContactRepository.findByKeyword(keyword,  pageable);
        }
        return emergencyContactRepository.findAll(pageable);
    }


}
