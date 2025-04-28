package com.example.HealthCare.mapper;

import com.example.HealthCare.dto.response.AppointmentResponse;
import com.example.HealthCare.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);
    AppointmentResponse toAppointmentResponse(Appointment appointment);

    default Page<AppointmentResponse> toAppointmentsResponse(Page<Appointment> appointments) {
        return appointments.map(this::toAppointmentResponse);
    }
}