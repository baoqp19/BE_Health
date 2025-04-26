package com.example.HealthCare.controller;

import com.example.HealthCare.Util.CustomPagination;
import com.example.HealthCare.Util.SecurityUtil;
import com.example.HealthCare.dto.response.VaccicationResponse;
import com.example.HealthCare.mapper.VaccicationMapper;
import com.example.HealthCare.model.Member;
import com.example.HealthCare.model.User;
import com.example.HealthCare.model.Vaccication;
import com.example.HealthCare.dto.request.vaccication.AddVaccinationRequest;
import com.example.HealthCare.dto.request.vaccication.UpdateVaccinationRequest;
import com.example.HealthCare.service.MemberService;
import com.example.HealthCare.service.UserService;
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

    private final UserService userService;
    private final VaccicationMapper vaccinationMapper;

    public VaccinationController(
            VaccinationService vaccinationService,
            MemberService memberService,
            UserService userService,
            VaccicationMapper vaccinationMapper) {
        this.vaccinationService = vaccinationService;
        this.memberService = memberService;
        this.userService = userService;
        this.vaccinationMapper = vaccinationMapper;
    }

    @PostMapping("/vaccinations")
    public ResponseEntity<Vaccication> addVaccination(
            @Valid @RequestBody AddVaccinationRequest addVaccinationRequest) {
        Member checkMember = memberService.getMemberById(addVaccinationRequest.getMemberID());

        if (checkMember == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }

        Vaccication vaccination = Vaccication.builder()
                .member(checkMember)
                .vaccineName(addVaccinationRequest.getVaccineName())
                .dateAdministered(addVaccinationRequest.getDateAdministered())
                .build();
        Vaccication createdVaccination = this.vaccinationService.addVaccication(vaccination);

        return new ResponseEntity<>(createdVaccination, HttpStatus.OK);
    }

    @PutMapping("/vaccinations/{id}")
    public ResponseEntity<Vaccication> updateVaccination(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateVaccinationRequest updateVaccinationRequest) {

        Member checkMember = memberService.getMemberById(updateVaccinationRequest.getMemberID());

        if (checkMember == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }
        Vaccication vaccination = Vaccication.builder()
                .vaccinationID(id)
                .member(checkMember)
                .vaccineName(updateVaccinationRequest.getVaccineName())
                .dateAdministered(updateVaccinationRequest.getDateAdministered())
                .build();

        Vaccication updatedVaccination = this.vaccinationService.updateVaccication(vaccination);

        return new ResponseEntity<>(updatedVaccination, HttpStatus.OK);
    }

    @DeleteMapping("/vaccinations/{id}")
    public ResponseEntity<String> deleteVaccination(@PathVariable("id") Integer id) {
        this.vaccinationService.deleteVaccication(id);
        return ResponseEntity.ok().body("Xóa thành công");
    }

    @GetMapping("/vaccinations/{id}")
    public ResponseEntity<Vaccication> getMemberById(@PathVariable("id") Integer id) {
        Vaccication vaccination = this.vaccinationService.getVaccicationById(id);

        return new ResponseEntity<>(vaccination, HttpStatus.OK);
    }

    @GetMapping("/vaccinations")
    public ResponseEntity<CustomPagination<VaccicationResponse>> getAllMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long memberId) {

        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        User user = this.userService.handleGetUserByEmail(email);

        Page<Vaccication> vaccicationPage = vaccinationService.getAllVaccications(page, size, user.getId() ,keyword, memberId);
        Page<VaccicationResponse> vaccicationResponsePage = vaccicationPage.map(vaccinationMapper::toVaccicationResponse);
        CustomPagination<VaccicationResponse> vaccinationContent = new CustomPagination<>(vaccicationResponsePage);

        return new ResponseEntity<>(vaccinationContent, HttpStatus.OK);
    }

}
