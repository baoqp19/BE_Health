package com.example.HealthCare.dto.request.vaccication;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateVaccinationRequest {

    private Integer vaccinationId;

    @NotNull(message = "MemberId is required")
    private Integer memberId;

    @NotBlank(message = "VaccineName is required")
    private String vaccineName;

    @NotNull(message = "DateAdministered is required")
    private Date dateAdministered;

}