package com.Coffee.repository.admin;

import com.Coffee.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long> {
   // List<Branch> findByIsDeletedFalse();
}
