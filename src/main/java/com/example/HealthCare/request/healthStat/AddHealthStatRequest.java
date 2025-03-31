package com.example.HealthCare.request.healthStat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddHealthStatRequest {

    private Integer statID;

    private Integer memberID;

    private String statType;

    @NotNull(message = "Status value is required")
    private float statValue;

    @NotNull(message = "Date and time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.time.LocalDate date;

}