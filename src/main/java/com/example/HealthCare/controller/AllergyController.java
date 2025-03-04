package com.example.HealthCare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.HealthCare.Util.SercurityUtil;
import com.example.HealthCare.model.Allergy;
import com.example.HealthCare.model.User;
import com.example.HealthCare.request.allergy.AddAllergyRequest;
import com.example.HealthCare.request.allergy.UpdateAllergyRequest;
import com.example.HealthCare.service.AllergyService;
import com.example.HealthCare.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AllergyController {
    private final AllergyService allergyService;
    private final UserService userService;

    public AllergyController(AllergyService allergyService, UserService userService) {
        this.allergyService = allergyService;
        this.userService = userService;
    }

    @PostMapping("/allergies")
    public ResponseEntity<?> addAllergy(@Valid @RequestBody AddAllergyRequest addAllergyRequest) {

        String email = SercurityUtil.getCurrentUserLogin().isPresent() ? SercurityUtil.getCurrentUserLogin().get() : "";

        User user = this.userService.handleGetUserByEmail(email);

        Allergy allergy = Allergy.builder()
                .memberID(addAllergyRequest.getMemberID())
                .allergyType(addAllergyRequest.getAllergyType())
                .severity(addAllergyRequest.getSeverity())
                .symptoms(addAllergyRequest.getSymptoms())
                .build();

        log.info(allergy.toString());

        Allergy createdAllergy = this.allergyService.addAllergy(allergy);

        return new ResponseEntity<>(createdAllergy, HttpStatus.CREATED);
    }

    @PutMapping("/allergies/{id}")
    public ResponseEntity<?> updateAllergy(

            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateAllergyRequest updateAllergyRequest) {

        Allergy allergy = Allergy.builder()
                .allergyID(id)
                .memberID(updateAllergyRequest.getMemberID())
                .allergyType(updateAllergyRequest.getAllergyType())
                .severity(updateAllergyRequest.getSeverity())
                .symptoms(updateAllergyRequest.getSymptoms())
                .build();
        Allergy updatedAllergy = this.allergyService.updateAllergy(allergy);

        return new ResponseEntity<>(updatedAllergy, HttpStatus.OK);
    }

    @DeleteMapping("/allergies/{id}")
    public ResponseEntity<String> deleteAllergy(@PathVariable("id") Integer id) {
        this.allergyService.deleteAllergy(id);
        return ResponseEntity.ok().body("xóa thành công");
    }

    @GetMapping("/allergies/{id}")
    public ResponseEntity<?> getAllergyById(@PathVariable("id") Integer id) {

        Optional<Allergy> allergy = allergyService.findAllergyById(id);

        return new ResponseEntity<>(allergy, HttpStatus.OK);
    }

    @GetMapping("/allergies")
    public ResponseEntity<List<Allergy>> getAllAllergies(

            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {

        Page<Allergy> alleriesPage = allergyService.getAllAllergies(page, size, keyword);

        List<Allergy> allergies = alleriesPage.getContent();

        return new ResponseEntity<>(allergies, HttpStatus.OK);
    }

}
