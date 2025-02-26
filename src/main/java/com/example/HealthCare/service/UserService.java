package com.example.HealthCare.service;

import com.example.HealthCare.model.User;
import com.example.HealthCare.request.users.ChangePasswordRequest;
import java.security.Principal;
import java.util.Optional;

public interface UserService {

    public User handleGetUserByEmail(String username);

    public void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
