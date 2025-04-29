package com.example.HealthCare.mapper;


import com.example.HealthCare.dto.response.UserResponse;
import com.example.HealthCare.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    default Page<UserResponse> toUsersResponse(Page<User> users) {
        return users.map(this::toUserResponse);
    }

}










