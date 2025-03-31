package com.example.HealthCare.controller;

import com.example.HealthCare.Util.SercurityUtil;
import com.example.HealthCare.model.HealthStat;
import com.example.HealthCare.model.Member;
import com.example.HealthCare.model.User;
import com.example.HealthCare.request.healthStat.AddHealthStatRequest;
import com.example.HealthCare.request.healthStat.UpdateHealthStatRequest;
import com.example.HealthCare.response.ApiResponse;
import com.example.HealthCare.service.HealthStatService;
import com.example.HealthCare.service.MemberService;
import com.example.HealthCare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class HealthStatController {
    private final UserService userService;
    private final HealthStatService healthStatService;
    private final MemberService memberService;


    public HealthStatController(UserService userService, HealthStatService healthStatService, MemberService memberService) {
        this.healthStatService = healthStatService;
        this.userService = userService;
        this.memberService = memberService;
    }

    @PostMapping("/health-stats")
    public ResponseEntity<HealthStat> addHealthStat
            (@Valid @RequestBody AddHealthStatRequest addHealthStatRequest) {

         Member member =  this.memberService.getMemberById(addHealthStatRequest.getMemberID());

        if(member == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        HealthStat healthStat = HealthStat.builder()
                .memberID(addHealthStatRequest.getMemberID())
                .statType(addHealthStatRequest.getStatType())
                .statValue(addHealthStatRequest.getStatValue())
                .date(addHealthStatRequest.getDate())
                .build();
        HealthStat addHealthStat = healthStatService.addHealthStat(healthStat);

        return new ResponseEntity<>(addHealthStat, HttpStatus.OK);
    }

    @PutMapping("/health-stats/{id}")
    public ResponseEntity<HealthStat> updateVaccination(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateHealthStatRequest updateHealthStatRequest) {
        HealthStat healthStat = HealthStat.builder()
                .statID(id)
                .statType(updateHealthStatRequest.getStatType())
                .statValue(updateHealthStatRequest.getStatValue())
                .date(updateHealthStatRequest.getDate())
                .build();
        HealthStat updateHealthStat = healthStatService.updateHealthStat(healthStat);

        return new ResponseEntity<>(updateHealthStat, HttpStatus.OK);
    }

    @DeleteMapping("/health-stats/{id}")
    public ResponseEntity<String> deleteHealthStat(@PathVariable("id") Integer id) {
        healthStatService.deleteHealthStat(id);
        return ResponseEntity.ok().body("Health stat deleted successfully");
    }

    @GetMapping("/health-stats/{id}")
    public ResponseEntity<HealthStat> getHealthStatById(@PathVariable("id") Integer id) {
        HealthStat healthStat = healthStatService.getHealthStatById(id);

        return new ResponseEntity<>(healthStat, HttpStatus.OK);
    }

    @GetMapping("/health-stats")
    public ResponseEntity<List<HealthStat>> getAllHealthStatsByStatType(
            @RequestParam(defaultValue = "") Integer selectedMemberId,
            @RequestParam(defaultValue = "") String selectedStatType,
            @RequestParam(defaultValue = "") String date) {

        List<HealthStat> healthStatList = healthStatService.getHealthStatsByStatType(selectedMemberId, selectedStatType, date);

        return new ResponseEntity<>(healthStatList, HttpStatus.OK);
    }

    @GetMapping("/health-stats/membersSelect")
    public ResponseEntity<List<Member>> getAllMembers() {
      String email = SercurityUtil.getCurrentUserLogin().isPresent() ? SercurityUtil.getCurrentUserLogin().get() : null;
        User user = this.userService.handleGetUserByEmail(email);

        List<Member> membersList = memberService.getAllMembersByUserID(user.getId());

        return new ResponseEntity<>(membersList, HttpStatus.OK);
    }

    @GetMapping("/health-stats/displayChart")
    public ResponseEntity<List<HealthStat>> getHealthStatsToDisplayChart(
            @RequestParam(defaultValue = "") Integer selectedMemberId,
            @RequestParam(defaultValue = "") String selectedStatType,
            @RequestParam(defaultValue = "") String date) {

        List<HealthStat>  healthStatList = healthStatService.getHealthStatsToDisplayChart(selectedMemberId, selectedStatType, date);

        return new ResponseEntity<>(healthStatList, HttpStatus.OK);
    }
}
