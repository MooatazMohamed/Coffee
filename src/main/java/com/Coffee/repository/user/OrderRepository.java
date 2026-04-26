package com.Coffee.repository.user;

import com.Coffee.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);

}
