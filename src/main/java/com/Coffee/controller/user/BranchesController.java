package com.Coffee.controller.user;

import com.Coffee.dto.user.response.UserBranchResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.service.admin.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchesController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserBranchResponse>>> getAllBranches(){
        List<UserBranchResponse> branches = branchService.getAllBranchesForUsers();
        return ResponseEntity
                .ok(ApiResponse.success("Branches fetched successfully", branches));
    }
}
