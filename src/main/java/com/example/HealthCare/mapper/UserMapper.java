package com.example.HealthCare.mapper;


import com.example.HealthCare.dto.response.UserResponse;
import com.example.HealthCare.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "is_verify", source = "_verify")
    @Mapping(target = "is_block", source = "_block")
    UserResponse toUserResponse(User user);

    default Page<UserResponse> toUsersResponse(Page<User> users) {
        return users.map(this::toUserResponse);
    }

}










