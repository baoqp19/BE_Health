package com.example.HealthCare.service.impl;

import com.example.HealthCare.Util.Const;
import com.example.HealthCare.dto.SendMail.DataMailDTO;
import com.example.HealthCare.model.User;
import com.example.HealthCare.repository.UserRepository;
import com.example.HealthCare.dto.request.auth.RegisterRequest;
import com.example.HealthCare.dto.request.users.ChangePasswordRequest;
import com.example.HealthCare.dto.response.AuthenticationResponse;
import com.example.HealthCare.dto.response.UserResponse;
import com.example.HealthCare.service.MailService;
import com.example.HealthCare.service.UserService;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@Builder
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

    @Override
    public User handleGetUserByEmail(String username) {
        return this.userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("User not found"));
    }

    @Override
    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByEmail(email);

        currentUser.setRefreshToken(token);
        this.userRepository.save(currentUser);
    }

    @Override
    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    @Override
    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User handleCreateUser(RegisterRequest registerRequest) {
        User user = User.builder()
                .email(registerRequest.getEmail())
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .password(registerRequest.getPassword())
                .role(registerRequest.getRole())
                .build();
        return this.userRepository.save(user);
    }

    @Override
    public void verifyEmail(String email) {
        var user = this.userRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("User not found"));

        user.set_verify(true);
        this.userRepository.save(user);
    }

    public AuthenticationResponse register(RegisterRequest request) {

        if(this.userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = this.userRepository.save(user);
        // Gửi mail xác nhận
        try {
            DataMailDTO dataMail = new DataMailDTO();

            dataMail.setTo(request.getEmail());
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_REGISTER);

            Map<String, Object> props = new HashMap<>();
            props.put("name", request.getFirstname());
            props.put("username", request.getEmail());
            props.put("password", request.getPassword());
            dataMail.setProps(props);

            mailService.sendHTMLMail(dataMail, Const.TEMPLATE_FILE_NAME.CLIENT_REGISTER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tạo đối tượng UserResponse để đưa vào phản hồi
        UserResponse userResponse = UserResponse.builder()
                .firstname(savedUser.getFirstname())
                .lastname(savedUser.getLastname())
                .email(savedUser.getEmail())
                .build();

        return AuthenticationResponse.builder()
                .user(userResponse)
                .build();
    }


}
