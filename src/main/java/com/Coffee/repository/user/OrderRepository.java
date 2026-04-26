package com.Coffee.repository.user;

import com.Coffee.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);

    @Query("""
    SELECT o FROM Order o
    LEFT JOIN o.orderItems oi
    LEFT JOIN oi.productSize ps
    LEFT JOIN ps.product p
    LEFT JOIN oi.addons a
    WHERE o.branch.id = :branchId
    AND o.isDeleted = false
""")
    List<Order> getOrdersByBranchId(Long branchId);
}
