package com.example.HealthCare.service;

import com.example.HealthCare.model.User;
import com.example.HealthCare.request.auth.RegisterRequest;
import com.example.HealthCare.request.users.ChangePasswordRequest;
import java.security.Principal;

public interface UserService {

    public User handleGetUserByEmail(String username);

    public void changePassword(ChangePasswordRequest request, Principal connectedUser);

    public void updateUserToken(String token, String email);

    public User getUserByRefreshTokenAndEmail(String token, String email);


    boolean isEmailExist(String email);

    public User handleCreateUser(RegisterRequest registerRequest);

    
}
