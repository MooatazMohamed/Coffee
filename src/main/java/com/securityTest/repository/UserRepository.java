package com.securityTest.repository;

import com.securityTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {
    User findByEmail(String email);
}
