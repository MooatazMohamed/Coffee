package com.securityTest.mapper;

import com.securityTest.dto.SignRequest;
import com.securityTest.dto.UserResponse;
import com.securityTest.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignRequest dto);

    @Mapping(source = "role.name", target = "role")
    UserResponse toResponse(User user);
}