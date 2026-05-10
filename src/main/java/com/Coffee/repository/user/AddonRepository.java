package com.Coffee.repository.user;

import com.Coffee.dto.admin.response.AddonResponse;
import com.Coffee.model.Addon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddonRepository extends JpaRepository<Addon, Long> {

    List<Addon> findByIsAvailableTrueAndIsDeletedFalse();

    @Query("SELECT a FROM Addon a " +
            "JOIN a.categories c " +
            "WHERE c.id = :categoryId AND a.isAvailable = true AND a.isDeleted = false")
    List<Addon> findAddonsByCategoryId(@Param("categoryId") Long categoryId);

    List<Addon> findByIdInAndIsAvailableTrueAndIsDeletedFalse(List<Long> ids);

    @Query("""
    SELECT DISTINCT a
    FROM Addon a
    LEFT JOIN FETCH a.categories
    WHERE a.isDeleted = false
    AND ( :search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) )
""")
    List<Addon> getAndSearchAddons(@Param("search") String search);

    List<Addon> findByIsDeletedFalse();
}
