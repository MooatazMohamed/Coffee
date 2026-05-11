package com.Coffee.service.admin;

import com.Coffee.dto.admin.request.BranchRequestDTO;
import com.Coffee.dto.admin.response.BranchResponseDTO;
import com.Coffee.dto.admin.response.BranchesOrder;
import com.Coffee.dto.user.response.UserBranchResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.mapper.admin.BranchMapper;
import com.Coffee.mapper.user.UserMapper;
import com.Coffee.model.Branch;
import com.Coffee.model.Role;
import com.Coffee.model.User;
import com.Coffee.model.UserPrincipal;
import com.Coffee.repository.admin.BranchRepository;
import com.Coffee.repository.user.RoleRepository;
import com.Coffee.repository.user.UserRepository;
import com.Coffee.service.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
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


    // get active branch
    public List<BranchResponseDTO> getAllBranches() {
    return branchRepository.findAll()
            .stream()
            .map(branchMapper::branchEntitytoDto)
            .toList();
    }

    public BranchResponseDTO getBranchWithUsers(Long branchId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal user =
                (UserPrincipal) authentication.getPrincipal();

        boolean isManager = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MANAGER"));

        Branch branch;

        if (isManager) {

            Long managerBranchId = user.getBranchId();

            branch = branchRepository.findById(managerBranchId)
                    .orElseThrow(() -> new RuntimeException("Branch not found"));

        } else {
            if (branchId == null) {
                throw new RuntimeException("branchId is required for ADMIN");
            }
            branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
        }

        return branchMapper.branchEntitytoDto(branch);
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

   // Branch updateBranch = branchMapper.branchDtoToEntity(branchRequestDTO);
    //  this.branchMapper.branchDtoToEntity(clubRequestDTO,branch);

        branch.setName(branchRequestDTO.name());
        branch.setAddress(branchRequestDTO.address());
        branch.setPhone(branchRequestDTO.phone());

        Branch saveBranch =  this.branchRepository.save(branch);
    return this.branchMapper.branchEntitytoDto(saveBranch);
    }// end of update method


    //@Transactional  // because lazy in branch entity
    public  void deleteBranch(Long branchId) throws IOException {
        Branch branch = this.branchRepository.findById(branchId).orElseThrow(()-> new RuntimeException("Branch not found"));


    //  branch.getUsers().forEach(user -> user.setIsDeleted(true));
    userRepository.softDeleteUsersByBranchId(branchId); //delete from database is best
    branch.setIsDeleted(true);
        this.branchRepository.save(branch);

    //        this.clubRepository.deleteById(club.getId());
    } // end of deleted branch


    public ApiResponse<Page<BranchesOrder>> getAllOrdersForAllBranches(
            Long branchId, String search, LocalDate date , int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BranchesOrder> ordersPage = branchRepository
                .getAllOrdersForAllBranches(branchId, search, date , pageable);
        return ApiResponse.success("Data Retrieved Success", ordersPage);
    }


    // ─── User Use Cases ─────────────────────────────

    public List<UserBranchResponse> getAllBranchesForUsers(){
        List<Branch> branches = branchRepository.findByIsDeletedFalse();
        return branchMapper.toBranchResponseList(branches);
    }
}
