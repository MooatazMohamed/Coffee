package com.Coffee.service.admin;

import com.Coffee.dto.admin.request.BranchRequestDTO;
import com.Coffee.dto.admin.response.BranchResponseDTO;
import com.Coffee.dto.admin.response.BranchesOrder;
import com.Coffee.handler.ApiResponse;
import com.Coffee.mapper.admin.BranchMapper;
import com.Coffee.mapper.user.UserMapper;
import com.Coffee.model.Branch;
import com.Coffee.model.Role;
import com.Coffee.model.User;
import com.Coffee.repository.admin.BranchRepository;
import com.Coffee.repository.user.RoleRepository;
import com.Coffee.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class BranchService {

    @Autowired
    BranchMapper branchMapper;
    @Autowired
    UserMapper userMapper;
  @Autowired
  PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BranchRepository branchRepository;

    @Transactional
// get active branch
public List<BranchResponseDTO> getAllBranches() {
    return branchRepository.findAll()
            .stream()
            .map(branchMapper::branchEntitytoDto)
            .toList();
}

// create branch method
public BranchResponseDTO  createBranch(BranchRequestDTO branchRequestDTO){

      Branch branch = branchMapper.branchDtoToEntity(branchRequestDTO);

      User getUser = userMapper.toEntity(branchRequestDTO.branchAdmin()); // entity

     // getUser.setBranch(branch);

      Role branchAdminRole = roleRepository.findByName("MANAGER")
              .orElseThrow(() -> new RuntimeException("Role MANAGER not found"));

      getUser.setRole(branchAdminRole);// return role  entity
      getUser.setPassword(passwordEncoder.encode(getUser.getPassword()));

      if(userRepository.existsByEmail(getUser.getEmail())) {
          throw new RuntimeException("Email already exists");
      }
// uni direction  one direction
//      Branch savedBranch = branchRepository.save(branch);
//      getUser.setBranch(savedBranch);
//      userRepository.save(getUser);
//bi direction  two direction
      getUser.setBranch(branch);
      branch.setUsers(List.of(getUser));
      BranchResponseDTO branchDto = branchMapper.branchEntitytoDto(branchRepository.save(branch))  ;

      return branchDto;

  }  // end of create branch method

// update branch
public BranchResponseDTO updateBranch(Long branchId,BranchRequestDTO branchRequestDTO) throws IOException {
    Branch branch = this.branchRepository.findById(branchId).orElseThrow(()-> new RuntimeException("Branch not found"));

    Branch updateBranch = branchMapper.branchDtoToEntity(branchRequestDTO);
  //  this.branchMapper.branchDtoToEntity(clubRequestDTO,branch);

    Branch saveClub =  this.branchRepository.save(updateBranch);
    return this.branchMapper.branchEntitytoDto(saveClub);
}// end of update method


@Transactional  // because lazy in branch entity
public  void deleteBranch(Long branchId) throws IOException {
        Branch branch = this.branchRepository.findById(branchId).orElseThrow(()-> new RuntimeException("Branch not found"));


  //  branch.getUsers().forEach(user -> user.setIsDeleted(true));
    userRepository.softDeleteUsersByBranchId(branchId); //delete from database is best
    branch.setIsDeleted(true);
        this.branchRepository.save(branch);

//        this.clubRepository.deleteById(club.getId());
    } // end of deleted branch


    public ApiResponse<List<BranchesOrder>> getAllOrdersForAllBranches(Long branchId, String search){
        return ApiResponse.success(
                "Data Retrieved Success",
                branchRepository.getAllOrdersForAllBranches(branchId, search)
        );
    }


}
