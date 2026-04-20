package com.Coffee.repository.user;

import com.Coffee.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsDeletedFalse();
    List<Category> findByIsDeletedFalseOrderByName();
    Optional<Category> findByIdAndIsDeletedFalse(Long id);
}
