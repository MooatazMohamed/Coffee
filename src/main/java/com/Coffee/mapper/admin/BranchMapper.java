package com.Coffee.mapper.admin;

import com.Coffee.dto.UserResponse;
import com.Coffee.dto.admin.request.BranchRequestDTO;
import com.Coffee.dto.admin.response.BranchResponseDTO;
import com.Coffee.dto.auth.signup.SignRequest;
import com.Coffee.mapper.user.UserMapper;
import com.Coffee.model.Branch;
import com.Coffee.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {UserMapper.class})
public interface BranchMapper {
    Branch branchDtoToEntity(BranchRequestDTO dto);

    @Mapping(target = "branchAdmin", expression = "java(extractAdmin(branch))")
    @Mapping(target = "id", source = "id")
    BranchResponseDTO branchEntitytoDto(Branch branch);


    default UserResponse extractAdmin(Branch branch) {
        if (branch.getUsers() == null || branch.getUsers().isEmpty()) return null;
        // Assuming the first user added is the admin/manager
        User admin = branch.getUsers().get(0);
        return new UserResponse(admin.getEmail() ,admin.getName() , admin.getRole().getName());
    }


}

