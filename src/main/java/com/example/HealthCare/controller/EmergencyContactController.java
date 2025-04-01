package com.example.HealthCare.controller;


import com.example.HealthCare.Util.SercurityUtil;
import com.example.HealthCare.model.EmergencyContact;
import com.example.HealthCare.model.User;
import com.example.HealthCare.dto.request.emergencyContact.AddEmergencyContactRequest;
import com.example.HealthCare.dto.request.emergencyContact.UpdateEmergencyContactRequest;
import com.example.HealthCare.service.EmergencyContactService;
import com.example.HealthCare.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;
    private final UserService userService;

    public EmergencyContactController(EmergencyContactService emergencyContactService, UserService userService) {
        this.emergencyContactService = emergencyContactService;
        this.userService = userService;
    }

    @PostMapping("/emergencyContacts")
    public ResponseEntity<EmergencyContact> addEmeregencyContact(@Valid @RequestBody AddEmergencyContactRequest addEmergencyContactRequest){

        String email = SercurityUtil.getCurrentUserLogin().isPresent()
                ? SercurityUtil.getCurrentUserLogin().get()
                : "";

        User user = this.userService.handleGetUserByEmail(email);

        EmergencyContact emergencyContact = EmergencyContact.builder()
                .userID(user.getId())
                .name(addEmergencyContactRequest.getName())
                .relationship(addEmergencyContactRequest.getRelationship())
                .phoneNumber(addEmergencyContactRequest.getPhoneNumber())
                .build();

        log.info(emergencyContact.toString());

        EmergencyContact createdEmergencyContact = this.emergencyContactService.addEmergencyContact(emergencyContact);

        return new ResponseEntity<>(createdEmergencyContact, HttpStatus.OK);
    }

    @PutMapping("/emergencyContacts/{id}")


    public ResponseEntity<EmergencyContact> updateEmergencyContact(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateEmergencyContactRequest updateEmergencyContactRequest){

        EmergencyContact emergencyContact = EmergencyContact.builder()
                .contactID(id)
                .name(updateEmergencyContactRequest.getName())
                .relationship(updateEmergencyContactRequest.getRelationship())
                .phoneNumber(updateEmergencyContactRequest.getPhoneNumber())
                .build();
        EmergencyContact updateEmergencyContact = this.emergencyContactService.updateEmergencyContact(emergencyContact);

        return new ResponseEntity<>(updateEmergencyContact, HttpStatus.OK);

    }

    @DeleteMapping("/emergencyContacts/{id}")
    public ResponseEntity<String> deleteEmergencyContact(@PathVariable("id") Integer id){
        this.emergencyContactService.deleteEmergencyContact(id);
        return  ResponseEntity.ok("xoá thành công");
    }

    @GetMapping("/emergencyContacts")
    public  ResponseEntity<List<EmergencyContact>> getAllEmergencyContacts(
            @RequestParam(defaultValue =  "1") int page,
            @RequestParam(defaultValue =  "8") int size,
            @RequestParam(defaultValue =  "")  String keyword){

        Page<EmergencyContact> emergencyContactsPage = this.emergencyContactService.getAllEmergencyContacts(page, size, keyword);

        List<EmergencyContact> emergencyContactsContent = emergencyContactsPage.getContent();

        return new ResponseEntity<>(emergencyContactsContent, HttpStatus.OK);
    }

}
