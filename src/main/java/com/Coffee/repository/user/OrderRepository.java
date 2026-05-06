package com.Coffee.repository.user;

import com.Coffee.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
            AND o.isDeleted = false AND 
            (o.status = com.Coffee.enums.OrderStatus.PENDING OR o.status = com.Coffee.enums.OrderStatus.PREPARING) 
            AND(:search IS NULL OR :search = '' OR LOWER(o.orderNumber) LIKE LOWER(CONCAT('%' , :search , '%')) 
            OR CAST(o.id AS string) LIKE CONCAT('%' , :search , '%') ) """)
    List<Order> getOrdersByBranchId(Long branchId , String search);

    @Query("""
    SELECT DISTINCT o FROM Order o
    LEFT JOIN o.orderItems oi
    LEFT JOIN oi.productSize ps
    LEFT JOIN ps.product p
    LEFT JOIN oi.addons a
    WHERE o.branch.id = :branchId
    AND o.isDeleted = false
    AND o.status = com.Coffee.enums.OrderStatus.READY
    AND (:search IS NULL OR :search = '' 
         OR LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%'))
         OR CAST(o.id AS string) LIKE CONCAT('%', :search, '%'))
""")
    List<Order> searchOrdersByBranchIdAndReady(Long branchId, String search);

    @Query("""
    SELECT DISTINCT o FROM Order o
    LEFT JOIN o.orderItems oi
    LEFT JOIN oi.productSize ps
    LEFT JOIN ps.product p
    LEFT JOIN oi.addons a
    WHERE o.branch.id = :branchId
    AND o.isDeleted = false
    AND o.status = com.Coffee.enums.OrderStatus.COMPLETED
    AND (:search IS NULL OR :search = '' 
         OR LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%'))
         OR CAST(o.id AS string) LIKE CONCAT('%', :search, '%'))
""")
    List<Order> searchOrdersByBranchIdAndCompleted(Long branchId, String search);

    // ─── Dashboard Metrics Queries ─────────────────────────────

    /**
     * Calculate total revenue from completed orders (all branches)
     */
    @Query("""
    SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o
    WHERE o.isDeleted = false
    AND o.status = com.Coffee.enums.OrderStatus.COMPLETED
""")
    BigDecimal getTotalRevenue();

    /**
     * Get total count of all orders (all branches)
     */
    @Query("""
    SELECT COUNT(o) FROM Order o
    WHERE o.isDeleted = false
""")
    Long getTotalOrdersCount();

    /**
     * Get recent orders for dashboard (last 10)
     */
    @Query("""
    SELECT o FROM Order o
    LEFT JOIN FETCH o.user
    LEFT JOIN FETCH o.branch
    WHERE o.isDeleted = false
    ORDER BY o.createdAt DESC
    LIMIT 10
""")
    List<Order> getRecentOrders();
}
