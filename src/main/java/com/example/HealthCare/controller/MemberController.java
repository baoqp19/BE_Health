package com.example.HealthCare.controller;

import com.example.HealthCare.Util.SercurityUtil;
import com.example.HealthCare.dto.PaginationDTO.ResultPaginationDTO;
import com.example.HealthCare.model.Member;
import com.example.HealthCare.model.User;
import com.example.HealthCare.request.member.AddMemberRequest;
import com.example.HealthCare.response.ApiResponse;
import com.example.HealthCare.service.MemberService;
import com.example.HealthCare.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class MemberController {

    private final UserService userService;
    private final MemberService memberService;

    public MemberController(UserService userService, MemberService memberService) {
        this.memberService = memberService;
        this.userService = userService;
    }

    @PostMapping("/members")
    public ResponseEntity<?> addMember(@Valid @RequestBody AddMemberRequest addMemberRequest) {

        String email = SercurityUtil.getCurrentUserLogin().isPresent()
                ? SercurityUtil.getCurrentUserLogin().get()
                : "";

        User user = this.userService.handleGetUserByEmail(email);

        Member member = Member.builder()
                .userID(user.getId())
                .fullName(addMemberRequest.getFullName())
                .dateOfBirth(addMemberRequest.getDateOfBirth())
                .gender(addMemberRequest.getGender().name())
                .relationship(addMemberRequest.getRelationship())
                .bloodType(addMemberRequest.getBloodType().name())
                .height(addMemberRequest.getHeight())
                .weight(addMemberRequest.getWeight())
                .build();
        log.info(member.toString());

        Member createdMember = this.memberService.addMember(member);

        return new ResponseEntity<>(createdMember, HttpStatus.OK);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<Member> updateMember(@Valid @PathVariable("id") Integer id, @RequestBody Member member) {
        member.setMemberID(id);
        Member updatedMember = this.memberService.updateMember(member);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@Valid @PathVariable("id") Integer id) {
        this.memberService.deleteMember(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable("id") Integer id) {
        Member member = memberService.getMemberById(id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @GetMapping("/members")
    public ResponseEntity<ResultPaginationDTO> getAllMember(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {

        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        int current = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(current - 1, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(this.memberService.getAllMember(pageable));
    }
}
