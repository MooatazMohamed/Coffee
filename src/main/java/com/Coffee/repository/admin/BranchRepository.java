package com.Coffee.repository.admin;

import com.Coffee.dto.admin.response.BranchesOrderView;
import com.Coffee.model.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long> {

    // ─── User Use Cases ─────────────────────────────
    List<Branch> findByIsDeletedFalse();

    Optional<Branch> findByIdAndIsDeletedFalse(Long id);

    // ─── Admin Use Cases ────────────────────────────


    // List<Branch> findByIsDeletedFalse();

    @Query(value = """
SELECT o.order_number as orderNumber,
       u.name as userName,
       b.name as branchName,
       o.total_price as totalPrice,
       o.type as type,
       o.created_at as createdAt,
       b.id as branchId,
       o.status as status
FROM orders o
LEFT JOIN users u ON u.id = o.user_id
LEFT JOIN branches b ON b.id = o.branch_id
WHERE (:branchId IS NULL OR b.id = :branchId)
AND (
    :search IS NULL OR 
    LOWER(o.order_number::text) LIKE LOWER('%' || :search || '%') OR
    LOWER(u.name::text) LIKE LOWER('%' || :search || '%') OR
    LOWER(b.name::text) LIKE LOWER('%' || :search || '%')
)
AND (:date IS NULL OR DATE(o.created_at) = :date)
ORDER BY o.created_at DESC
""", nativeQuery = true, countQuery = """
SELECT COUNT(*)
FROM orders o
LEFT JOIN users u ON u.id = o.user_id
LEFT JOIN branches b ON b.id = o.branch_id
WHERE (:branchId IS NULL OR b.id = :branchId)
AND (
    :search IS NULL OR 
    LOWER(o.order_number::text) LIKE LOWER('%' || :search || '%') OR
    LOWER(u.name::text) LIKE LOWER('%' || :search || '%') OR
    LOWER(b.name::text) LIKE LOWER('%' || :search || '%')
)
AND (:date IS NULL OR DATE(o.created_at) = :date)
""")
    Page<BranchesOrderView> getAllOrdersForAllBranches(
            @Param("branchId") Long branchId,
            @Param("search") String search,
            @Param("date") LocalDate date,
            Pageable pageable
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
