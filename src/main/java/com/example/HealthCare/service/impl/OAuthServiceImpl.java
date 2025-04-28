package com.example.HealthCare.service.impl;

import com.example.HealthCare.context.OAuthContext;
import com.example.HealthCare.dto.response.AuthenticationResponse;
import com.example.HealthCare.dto.response.OAuthUser;
import com.example.HealthCare.enums.Role;
import com.example.HealthCare.model.User;
import com.example.HealthCare.repository.UserRepository;
import com.example.HealthCare.service.OAuthService;
import com.example.HealthCare.service.UserService;
import com.example.HealthCare.strategies.GoogleAuthStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class OAuthServiceImpl implements OAuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    public OAuthServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public AuthenticationResponse authenticate(String credential) {
        log.info("Google login credential: {}", credential);
        OAuthContext oAuthContext = new OAuthContext(new GoogleAuthStrategy());
        OAuthUser oauthUser = oAuthContext.authenticate(credential);
        Optional<User> checkUser = userRepository.findByEmail(oauthUser.getEmail());
        if (checkUser.isEmpty()) {
            String[] nameParts = oauthUser.getName().split(" ", 2);
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            var user = User.builder()
                    .email(oauthUser.getEmail())
                    .firstname(firstName)
                    .lastname(lastName)
                    .role(Role.USER)
                    .is_verify(true)
                    .is_block(false)
                    .build();
            userRepository.save(user);
        }
        return userService.authenticateWithEmail(oauthUser.getEmail());
    }
}
