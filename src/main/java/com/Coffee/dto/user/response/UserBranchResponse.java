package com.Coffee.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserBranchResponse {
    private Integer id;
    private String name;
    private String address;
}
