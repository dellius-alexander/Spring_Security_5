package com.example.spring_security_web_service.User;


import java.util.Optional;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Data Access Layer
 */
@Repository  // Provide value for @Autowired dependency injection instead of using @Component
@ComponentScan(basePackages = {
        "com.example.spring_security_web_service.User"
})  // Use Component scan for @Autowired dependency injection
public interface UserRepository extends JpaRepository<User, Long> {
    // @Query provides a way to customize the query to findByUsername
    @Query("SELECT u FROM User u WHERE u.name LIKE %?1%")
    Optional<User> findUserByName(String name);

    @Query("SELECT u FROM User u WHERE u.username like %?1")
    Optional<User> findUserByUsername(String username);

    @Query("DELETE FROM User u WHERE u.username = ?1")
    boolean deleteByUsername(String username);

}
