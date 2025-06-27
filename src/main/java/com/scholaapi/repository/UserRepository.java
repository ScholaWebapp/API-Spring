package com.scholaapi.repository;

// src/main/java/com/schola/repository/UserRepository.java

import com.scholaapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    void deleteByUuid(UUID uuid);
//    default List<User> getUsers() {
//        return findAll();
//    }
}