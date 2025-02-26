package com.example.HealthCare.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewStudentDetails {
    private String name;
    private String email;
    private LocalDate birthDate;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
}