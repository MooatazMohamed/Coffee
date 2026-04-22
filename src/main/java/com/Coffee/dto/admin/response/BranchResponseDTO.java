package com.Coffee.dto.admin.response;

import com.Coffee.dto.UserResponse;
import com.Coffee.dto.auth.signup.SignRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BranchResponseDTO(
        @NotBlank(message = "Branch Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Address is required")
        @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
        String address,

        @NotBlank(message = "Phone is required")
        @Pattern(
                regexp = "^(010|011|012|015)[0-9]{8}$",
                message = "Phone must be 11 digits and start with 010, 011, 012, or 015"
        )
        String phone
        ,
        Boolean isDeleted
        ,
        @Valid
//    @NotNull
        UserResponse branchAdmin  // dto response [name , email , role]
) {
}
