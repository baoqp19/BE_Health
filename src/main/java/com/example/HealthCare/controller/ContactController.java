package com.example.HealthCare.controller;

import com.example.HealthCare.Util.CustomPagination;
import com.example.HealthCare.dto.response.ApiResponse;
import com.example.HealthCare.dto.response.ContactResponse;
import com.example.HealthCare.model.Contact;
import com.example.HealthCare.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1")
@RestController
public class ContactController {
    private final ContactService contactService;


    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    public ResponseEntity<CustomPagination<ContactResponse>> getAllMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {
        Page<ContactResponse> contactsPage = this.contactService.getAllContacts(page, size, keyword);

        CustomPagination<ContactResponse> contactsContent = new CustomPagination<>(contactsPage);

        ApiResponse<CustomPagination<ContactResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Get list contacts successfully",
                contactsContent
        );
        return new ResponseEntity<>(contactsContent, HttpStatus.OK);
    }

    @PutMapping("/contacts/{id}")
    public ResponseEntity<ApiResponse<?>> updateBlockState(
            @PathVariable("id") Integer id) {
        Contact updateContact = this.contactService.updateSeenStateUser(id);
        ApiResponse<Contact> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Update seen state of contacts successfully",
                updateContact
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
