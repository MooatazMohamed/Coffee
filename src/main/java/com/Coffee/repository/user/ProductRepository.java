package com.Coffee.repository.user;

import com.Coffee.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIsAvailableTrueAndIsDeletedFalse();

    List<Product> findByCategoryIdAndIsAvailableTrueAndIsDeletedFalse(Long categoryId);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.productSizes ps " +
            "WHERE p.id = :id AND p.isDeleted = false")
    Optional<Product> findByIdWithSizes(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.category " +
            "WHERE p.isAvailable = true AND p.isDeleted = false")
    List<Product> findAllWithCategory();

    @Query("SELECT p FROM Product p " +
            "WHERE p.id = :id AND p.isAvailable = true AND p.isDeleted = false")
    Optional<Product> findByIdAndAvailable(@Param("id") Long id);

    List<Product> findTop8ByIsAvailableTrueAndIsDeletedFalseOrderByCreatedAtDesc();
}
