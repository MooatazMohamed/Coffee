package com.Coffee.repository.admin;

import com.Coffee.dto.admin.response.StaffResponse;
import com.Coffee.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<User,Long> {

    @Query("""
SELECT new com.Coffee.dto.admin.response.StaffResponse(
    u.id,
    u.name,
    u.email,
    r.id,
    r.name,
    b.id,
    b.name
)
FROM User u
LEFT JOIN u.role r
LEFT JOIN u.branch b
WHERE u.isDeleted = false
AND u.role.id IN (2, 3)
AND (:search IS NULL OR
    LOWER(u.name) LIKE LOWER(CONCAT('%' , :search , '%')) OR
    LOWER(u.email) LIKE LOWER(CONCAT('%' , :search , '%')) OR
    LOWER(r.name) LIKE LOWER(CONCAT('%' , :search , '%')) OR
    LOWER( b.name) LIKE LOWER(CONCAT('%' , :search , '%'))
)
""")
    List<StaffResponse> getStaff(@Param("search") String search);
}
