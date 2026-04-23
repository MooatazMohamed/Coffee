package com.Coffee.service.user.Impl;

import com.Coffee.dto.user.response.BranchResponse;
import com.Coffee.mapper.user.BranchMapper;
import com.Coffee.model.Branch;
import com.Coffee.repository.user.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public List<BranchResponse> getAllBranches(){
        List<Branch> branches = branchRepository.findByIsDeletedFalse();
        return branchMapper.toBranchResponseList(branches);
    }
}
