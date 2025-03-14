package com.academiajava.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import com.academiajava.backend.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findUserByUsername(String username);

    //@Query("SELECT u FROM User u WHERE u.username LIKE %:username% AND u.name LIKE %:name%")
    //List<User> findByNameAndUsername(@Param("name") String name,@Param("username") String username);
    List<User> findAllByNameContainingIgnoreCaseAndUsernameContainingIgnoreCase(String name, String username);


    List<User> findByNameContainsIgnoreCase(String name);

    List<User> findByUsernameContainsIgnoreCase(String username);
    
    UserDetails findByUsername(String username);
}
