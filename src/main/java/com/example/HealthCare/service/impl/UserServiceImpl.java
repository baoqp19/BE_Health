package com.example.HealthCare.service.impl;

import com.example.HealthCare.Util.Const;
import com.example.HealthCare.Util.SecurityUtil;
import com.example.HealthCare.dto.SendMail.DataMailDTO;
import com.example.HealthCare.dto.request.auth.ResTokenLogin;
import com.example.HealthCare.mapper.UserMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@Builder
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final UserMapper userMapper;
    private final SecurityUtil securityUtil;

    public UserServiceImpl(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            MailService mailService,
            UserMapper userMapper,
            SecurityUtil securityUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userMapper = userMapper;
        this.securityUtil = securityUtil;
    }

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


    @Override
    public Page<UserResponse> getAllUsers(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
             Page<UserResponse> userResponse = userMapper.toUsersResponse(userRepository.findByKeyword(keyword, pageable));
             log.info(userResponse.toString());
             return userResponse;

        }
        return userMapper.toUsersResponse(userRepository.findAll(pageable));
    }

    @Override
    public UserResponse updateBlockStateUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User is not found!!!"));
        user.set_block(!user.is_block());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public AuthenticationResponse authenticate(String credential) {
        return null;
    }

    public AuthenticationResponse authenticateWithEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow();
        if (!user.is_verify() || user.is_block()) {
            return null;
        }

        // Chuẩn bị dữ liệu user cho token
        ResTokenLogin.UserLogin userLogin = new ResTokenLogin.UserLogin();
        userLogin.setId(user.getId());
        userLogin.setEmail(user.getEmail());
        userLogin.setFirstName(user.getFirstname());
        userLogin.setLastName(user.getLastname());

        ResTokenLogin resTokenLogin = new ResTokenLogin();
        resTokenLogin.setUser(userLogin);

        // Tạo accessToken và refreshToken mới
        String accessToken = this.securityUtil.createAccessToken(user.getEmail(), resTokenLogin);
        String refreshToken = this.securityUtil.createRefreshToken(user.getEmail(), resTokenLogin);


        // Build UserResponse trả về
        UserResponse userResponse = UserResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();

        // Build AuthenticationResponse trả về
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }


}
