package com.Coffee.repository.admin;

import com.Coffee.dto.admin.response.BranchesOrder;
import com.Coffee.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long> {

    // ─── User Use Cases ─────────────────────────────
    List<Branch> findByIsDeletedFalse();

    Optional<Branch> findByIdAndIsDeletedFalse(Long id);

    // ─── Admin Use Cases ────────────────────────────


    // List<Branch> findByIsDeletedFalse();

    @Query("""
SELECT new com.Coffee.dto.admin.response.BranchesOrder(
    o.orderNumber,
    u.name,
    b.name,
    o.totalPrice,
    o.type,
    o.createdAt,
    b.id,
    o.status
)
FROM com.Coffee.model.Order o
LEFT JOIN o.user u
LEFT JOIN o.branch b
WHERE (:branchId IS NULL OR b.id = :branchId)
AND (
    :search IS NULL OR 
    LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR
    LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
    LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%'))
)
""")
    List<BranchesOrder> getAllOrdersForAllBranches(
            @Param("branchId") Long branchId,
            @Param("search") String search
    );

    // ─── Dashboard Metrics Queries ─────────────────────────────

    /**
     * Get count of active (non-deleted) branches
     */
    @Query("""
    SELECT COUNT(b) FROM Branch b
    WHERE b.isDeleted = false
""")
    Long getActiveBranchesCount();
}
