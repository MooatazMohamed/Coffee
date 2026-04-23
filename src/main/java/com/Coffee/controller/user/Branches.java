package com.Coffee.controller.user;

import com.Coffee.dto.user.response.BranchResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.model.Branch;
import com.Coffee.service.user.Impl.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class Branches {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getAllBranches(){
        List<BranchResponse> branches = branchService.getAllBranches();
        return ResponseEntity
                .ok(ApiResponse.success("Branches fetched successfully", branches));
    }
}
