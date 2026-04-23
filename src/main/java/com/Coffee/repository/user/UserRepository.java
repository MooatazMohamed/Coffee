package com.Coffee.repository.user;

import com.Coffee.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User , Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    // soft delete user
    @Modifying
    @Query("UPDATE User u SET u.isDeleted = true WHERE u.branch.id = :branchId")
    void softDeleteUsersByBranchId(Long branchId);
}
