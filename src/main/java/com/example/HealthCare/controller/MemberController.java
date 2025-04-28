package com.example.HealthCare.controller;

import com.example.HealthCare.Util.CustomPagination;
import com.example.HealthCare.Util.SecurityUtil;
import com.example.HealthCare.dto.response.ApiResponse;
import com.example.HealthCare.dto.response.MemberResponse;
import com.example.HealthCare.mapper.MemberMapper;
import com.example.HealthCare.model.Member;
import com.example.HealthCare.model.User;
import com.example.HealthCare.dto.request.member.AddMemberRequest;
import com.example.HealthCare.dto.request.member.UpdateMemberRequest;
import com.example.HealthCare.service.MemberService;
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
public class MemberController {

    private final UserService userService;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    public MemberController(UserService userService, MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.userService = userService;
        this.memberMapper = memberMapper;
    }

    @PostMapping("/members")
    public ResponseEntity<?> addMember(@Valid @RequestBody AddMemberRequest addMemberRequest) {

        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        User user = this.userService.handleGetUserByEmail(email);

        Member member = MemberMapper.INSTANCE.toMember(addMemberRequest);
        member.setUser(user);
        Member createdMember = this.memberService.addMember(member);

        return new ResponseEntity<>(createdMember, HttpStatus.OK);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<Member> updateMember(@Valid @PathVariable("id") Integer id,
            @RequestBody UpdateMemberRequest updateMemberRequest) {

        Member member = MemberMapper.INSTANCE.toMember(updateMemberRequest);
        member.setId(id);
        Member updatedMember = this.memberService.updateMember(member);

        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@Valid @PathVariable("id") Integer id) {
        this.memberService.deleteMember(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable("id") Integer id) {
        Member member = memberService.getMemberById(id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // @GetMapping("/members")
    // public ResponseEntity<ResultPaginationDTO> getAllMember(
    // @RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional) {

    // String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
    // String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() :
    // "";

    // int current = Integer.parseInt(sCurrent);
    // int pageSize = Integer.parseInt(sPageSize);

    // Pageable pageable = PageRequest.of(current - 1, pageSize);

    // return
    // ResponseEntity.status(HttpStatus.OK).body(this.memberService.getAllMember(pageable));
    // }
    
    
    @GetMapping("/members")
    public ResponseEntity<CustomPagination<MemberResponse>> getAllMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {

        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        

        User user = this.userService.handleGetUserByEmail(email);
        Page<Member> membersPage = memberService.getAllMembers(page, size, keyword, user.getId());

        Page<MemberResponse> memberResponses = memberMapper.toMembersResponse(membersPage);
        CustomPagination<MemberResponse> membersContent = new CustomPagination<>(memberResponses);

        return new ResponseEntity<>(membersContent, HttpStatus.OK);
    }

    @GetMapping("/members/all")
    public ResponseEntity<List<MemberResponse>> getAllMembersByUser() {

        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";


        User user = this.userService.handleGetUserByEmail(email);
        List<Member> members = memberService.getAllMembersByUserID(user.getId());
        List<MemberResponse> memberResponses = memberMapper.toMembersList(members);
        return new ResponseEntity<>(memberResponses, HttpStatus.OK);
    }

}
