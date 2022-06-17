package com.example.spring_security_web_service.User.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository(value = "RoleRepository")
public interface RoleRepository extends JpaRepository<Role, Long> {
    // @Query provides a way to customize the query to findByUsername
    @Query("SELECT r FROM Role r WHERE r.name = ?1")
    Optional<Role> findRoleByName(String role);
}
