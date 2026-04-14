package com.securityTest.dto;

import com.securityTest.model.Role;
import lombok.Data;

@Data
public class UserResponse {

    private String email;
    private String name;
    private String role;
}

