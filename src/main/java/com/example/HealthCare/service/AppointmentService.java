package com.example.HealthCare.service;

import com.example.HealthCare.model.Appointment;
import org.springframework.data.domain.Page;

public interface AppointmentService {
    Appointment addAppointment(Appointment appointment);
    Appointment updateAppointment(Appointment appointment);
    void deleteAppointment(Integer appointmentID);
    Appointment getAppointmentById(Integer appointmentID);
    Page<Appointment> getAllAppointments(int page, int size, String keyword);
}
