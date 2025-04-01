package com.example.HealthCare.dto.request.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddAppointmentRequest {
    @NotNull(message = "MemberId is required")
    private Integer memberID;

    @NotNull(message = "Time is required")
    private Date time;

    @NotBlank(message = "Doctor is required")
    private String doctor;

    @NotBlank(message = "Location is required")
    private String location;
}
