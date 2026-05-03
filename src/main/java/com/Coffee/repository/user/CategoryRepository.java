package com.Coffee.repository.user;

import com.Coffee.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsDeletedFalse();
    List<Category> findByIsDeletedFalseOrderByName();
    Optional<Category> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT c FROM Category c WHERE c.id = :id")
    @EntityGraph(attributePaths = "addons")
    Optional<Category> findByIdWithAddons(@Param("id") Long id);
}
