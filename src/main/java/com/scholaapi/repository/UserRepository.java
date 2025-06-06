package com.scholaapi.repository;

// src/main/java/com/schola/repository/UserRepository.java

import com.scholaapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}