package com.example.HealthCare.controller;

import com.example.HealthCare.model.Member;
import com.example.HealthCare.model.Vaccination;
import com.example.HealthCare.dto.request.vaccication.AddVaccinationRequest;
import com.example.HealthCare.dto.request.vaccication.UpdateVaccinationRequest;
import com.example.HealthCare.service.MemberService;
import com.example.HealthCare.service.VaccinationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class VaccinationController {

    private final VaccinationService vaccinationService;
    private final MemberService memberService;

    public VaccinationController(VaccinationService vaccinationService, MemberService memberService) {
        this.vaccinationService = vaccinationService;
        this.memberService = memberService;
    }

    @PostMapping("/vaccinations")
    public ResponseEntity<Vaccination> addVaccination(
            @Valid @RequestBody AddVaccinationRequest addVaccinationRequest) {
        Member checkMember = memberService.getMemberById(addVaccinationRequest.getMemberID());

        if (checkMember == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }

        Vaccination vaccination = Vaccination.builder()
                .member(checkMember)
                .vaccineName(addVaccinationRequest.getVaccineName())
                .dateAdministered(addVaccinationRequest.getDateAdministered())
                .build();
        Vaccination createdVaccination = this.vaccinationService.addVaccication(vaccination);

        return new ResponseEntity<>(createdVaccination, HttpStatus.OK);
    }

    @PutMapping("/vaccinations/{id}")
    public ResponseEntity<Vaccination> updateVaccination(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateVaccinationRequest updateVaccinationRequest) {

        Member checkMember = memberService.getMemberById(updateVaccinationRequest.getMemberID());

        if (checkMember == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }
        Vaccination vaccination = Vaccination.builder()
                .vaccinationID(id)
                .member(checkMember)
                .vaccineName(updateVaccinationRequest.getVaccineName())
                .dateAdministered(updateVaccinationRequest.getDateAdministered())
                .build();

        Vaccination updatedVaccination = this.vaccinationService.updateVaccication(vaccination);

        return new ResponseEntity<>(updatedVaccination, HttpStatus.OK);
    }

    @DeleteMapping("/vaccinations/{id}")
    public ResponseEntity<String> deleteVaccination(@PathVariable("id") Integer id) {
        this.vaccinationService.deleteVaccication(id);
        return ResponseEntity.ok().body("Xóa thành công");
    }

    @GetMapping("/vaccinations/{id}")
    public ResponseEntity<Vaccination> getMemberById(@PathVariable("id") Integer id) {
        Vaccination vaccination = this.vaccinationService.getVaccicationById(id);

        return new ResponseEntity<>(vaccination, HttpStatus.OK);
    }

    @GetMapping("/vaccinations")
    public ResponseEntity<List<Vaccination>> getAllMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {
        Page<Vaccination> vaccinationPage = vaccinationService.getAllVaccications(page, size, keyword);

        List<Vaccination> vaccinationContent = vaccinationPage.getContent();

        return new ResponseEntity<>(vaccinationContent, HttpStatus.OK);
    }

}
