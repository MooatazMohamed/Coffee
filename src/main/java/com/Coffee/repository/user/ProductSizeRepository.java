package com.Coffee.repository.user;

import com.Coffee.model.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

    @Query("SELECT ps FROM ProductSize ps " +
            "JOIN FETCH ps.product " +
            "WHERE ps.id = :id AND ps.isDeleted = false")
    Optional<ProductSize> findByIdWithProduct(@Param("id") Long id);

    @Query("""
        SELECT ps FROM ProductSize ps
        JOIN ps.product p
        WHERE ps.id = :id
        AND ps.isDeleted = false
        AND p.isDeleted = false
        AND p.isAvailable = true
    """)
    Optional<ProductSize> findActiveById(@Param("id") Long id);
}
