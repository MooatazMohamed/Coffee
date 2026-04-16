package com.Coffee.dto.login_sign;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3 , max = 20 , message = "Size between 3 and 20")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^(010|011|012|015)[0-9]{8}$",
            message = "Phone must be 11 digits and start with 010, 011, 012, or 015"
    )
    private String phone;
}
