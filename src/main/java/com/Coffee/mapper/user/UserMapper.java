package com.Coffee.mapper.user;

import com.Coffee.dto.login_sign.SignRequest;
import com.Coffee.dto.login_sign.UserResponse;
import com.Coffee.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignRequest dto);

    @Mapping(source = "role.name", target = "role")
    UserResponse toResponse(User user);
}