package com.Coffee.dto.admin.response;

import com.Coffee.dto.UserResponse;
import com.Coffee.dto.auth.signup.SignRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BranchResponseDTO(
        Long id,
        String name,
        String address,
        String phone,
        Boolean isDeleted,
        UserResponse branchAdmin  // dto response [name , email , role]

) {
}
