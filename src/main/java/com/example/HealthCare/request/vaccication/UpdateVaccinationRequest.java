package com.example.HealthCare.request.vaccication;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateVaccinationRequest {

    private Integer vaccinationID;

    @NotNull(message = "MemberId is required")
    private Integer memberID;

    @NotBlank(message = "VaccineName is required")
    private String vaccineName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_administered")
    private java.time.LocalDate dateAdministered;

}