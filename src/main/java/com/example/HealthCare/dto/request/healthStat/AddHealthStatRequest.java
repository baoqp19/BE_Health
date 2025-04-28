package com.example.HealthCare.dto.request.healthStat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddHealthStatRequest {

    private Integer id;

    private Integer memberId;

    private String statType;

    @NotNull(message = "Status value is required")
    private float statValue;

    @NotNull(message = "Date and time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.time.LocalDate date;
}