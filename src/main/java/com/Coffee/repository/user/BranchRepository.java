package com.Coffee.repository.user;


import com.Coffee.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByIsDeletedFalse();

    Optional<Branch> findByIdAndIsDeletedFalse(Long id);
}
