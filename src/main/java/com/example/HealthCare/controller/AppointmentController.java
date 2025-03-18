package com.example.HealthCare.controller;

import com.example.HealthCare.model.Appointment;
import com.example.HealthCare.model.Member;
import com.example.HealthCare.request.appointment.AddAppointmentRequest;
import com.example.HealthCare.request.appointment.UpdateAppointmentRequest;
import com.example.HealthCare.service.AppointmentService;
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
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final MemberService memberService;

    public AppointmentController(AppointmentService appointmentService, UserService userService, MemberService memberService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.memberService = memberService;
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> addAppointment(@RequestBody @Valid AddAppointmentRequest addAppointmentRequest) {
        Member member = memberService.getMemberById(addAppointmentRequest.getMemberID());
        Appointment appointment = Appointment.builder()

                .doctor(addAppointmentRequest.getDoctor())
                .time(addAppointmentRequest.getTime())
                .location(addAppointmentRequest.getLocation())
                .member(member)
                .build();
        Appointment createdAppointment = appointmentService.addAppointment(appointment);
        return new ResponseEntity<>(createdAppointment, HttpStatus.OK);
    }
    
    @PutMapping("/appointments/{id}")
    public ResponseEntity<Appointment> updateAppointment(  @PathVariable("id") Integer id,  @Valid @RequestBody UpdateAppointmentRequest updateAppointmentRequest) {
        Member member = memberService.getMemberById(updateAppointmentRequest.getMemberID());
        Appointment appointment = Appointment.builder()
                .appointmentID(id)
                .member(member)
                .doctor(updateAppointmentRequest.getDoctor())
                .time(updateAppointmentRequest.getTime())
                .location(updateAppointmentRequest.getLocation())
                .build();
        Appointment updatedAppointment = appointmentService.updateAppointment(appointment);
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") Integer id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().body("xóa thành công");
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getDocumentById(@PathVariable("id") Integer id) {
      Appointment appointment = appointmentService.getAppointmentById(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "8") int size,
            @RequestParam(name = "keyword", required = false) String keyword) {
        Page<Appointment> appointments = appointmentService.getAllAppointments(page, size, keyword);
        List<Appointment> appointmentsList = appointments.getContent();

        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

}
