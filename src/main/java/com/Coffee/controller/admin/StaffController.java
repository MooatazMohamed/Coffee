package com.Coffee.controller.admin;

import com.Coffee.dto.admin.response.AddonResponse;
import com.Coffee.dto.admin.response.StaffResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.service.admin.StaffServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffServices staffServices;

    public StaffController(StaffServices staffServices){
        this.staffServices=staffServices;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAllStaff(@RequestParam(required = false) String search){
        return ResponseEntity.ok(staffServices.getAllStaff(search));
    }

}
