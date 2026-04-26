package com.Coffee.controller.admin;

import com.Coffee.dto.admin.request.BranchRequestDTO;
import com.Coffee.dto.admin.response.BranchResponseDTO;
import com.Coffee.handler.ApiResponse;
import com.Coffee.service.admin.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/branch")
@PreAuthorize("hasRole('ADMIN')")
public class BranchController {

@Autowired
    BranchService branchService;

    @GetMapping("")
    public ApiResponse<List<BranchResponseDTO>> AllBranches(){
        return ApiResponse.success("Branchs retrivied successfully",branchService.getAllBranches());
        // HttpStatus.OK

    }



    @PostMapping(value = "")
    public ApiResponse<BranchResponseDTO> createBranch(@Valid  @RequestBody BranchRequestDTO branchRequestDTO) throws IOException {
        return ApiResponse.success("Branch created successfully", branchService.createBranch(branchRequestDTO));
        //, HttpStatus.CREATED
    }


    @PutMapping(value = "{branchId}")
    public ApiResponse<BranchResponseDTO> updateBranch(@PathVariable Long branchId,@Valid  @RequestBody BranchRequestDTO branchRequestDTO) throws IOException {
        return ApiResponse.success("Branch updated successfully", branchService.updateBranch(branchId,branchRequestDTO));
        //, HttpStatus.CREATED
    }

    @DeleteMapping("{branchId}")
    public ApiResponse<String> deleteBranch(@PathVariable Long branchId) throws IOException {
        branchService.deleteBranch(branchId);
        return ApiResponse.success("Branch deleted successfully",null); // ,HttpStatus.OK
    }

}
