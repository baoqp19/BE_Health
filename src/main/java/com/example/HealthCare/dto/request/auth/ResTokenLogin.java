package com.example.HealthCare.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResTokenLogin {

    @JsonProperty("access_token") // ánh xạ muốn tên hiển trị API trả ra là access_token thì dùng
    private String accessToken;

    private UserLogin user;

    // static quan trong vra
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLogin {
        private long id;
        private String email;
        private String firstName;
        private String lastName;
    }
}
