package com.Coffee.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {

    private Long userId;
    private String userName;
    private String email;
    private Long role_id;
    private String role_name;
    private Long branchId;
    private String branchName;

}
