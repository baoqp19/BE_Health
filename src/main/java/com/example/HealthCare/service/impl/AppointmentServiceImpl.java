package com.example.HealthCare.service.impl;


import com.example.HealthCare.model.Appointment;
import com.example.HealthCare.repository.AppointmentRepository;
import com.example.HealthCare.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment addAppointment(Appointment appointment) {
        return this.appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Appointment appointment) {
        Appointment check = this.appointmentRepository.findById(appointment.getAppointmentID())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        return this.appointmentRepository.save(appointment);
    }

    @Override
    public void deleteAppointment(Integer appointmentID) {
        Appointment check = this.appointmentRepository.findById(appointmentID)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        this.appointmentRepository.deleteById(appointmentID);
    }

    @Override
    public Appointment getAppointmentById(Integer appointmentID) {
        return this.appointmentRepository.findById(appointmentID)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
    }

    @Override
    public Page<Appointment> getAllAppointments(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return this.appointmentRepository.findByKeyword(keyword, pageable);
        }
        return appointmentRepository.findByKeyword(keyword, pageable);
    }
}



