package com.Coffee.mapper.user;

import com.Coffee.dto.user.response.BranchResponse;
import com.Coffee.model.Branch;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    BranchResponse toBranchResponse(Branch branch);

    List<BranchResponse> toBranchResponseList(List<Branch> branches);
}
