package com.example.HealthCare.controller;

import com.example.HealthCare.Util.CustomPagination;
import com.example.HealthCare.dto.request.users.ChangePasswordRequest;
import com.example.HealthCare.dto.response.ApiResponse;
import com.example.HealthCare.dto.response.UserResponse;
import com.example.HealthCare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<CustomPagination<UserResponse>> getAllMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {
        Page<UserResponse> usersPage = this.userService.getAllUsers(page, size, keyword);

        CustomPagination<UserResponse> usersContent = new CustomPagination<>(usersPage);

        return new ResponseEntity<>(usersContent, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateBlockState(
            @PathVariable("id") Integer id) {
        UserResponse updateUser = this.userService.updateBlockStateUser(id);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @PatchMapping("/users")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser) {
        this.userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
