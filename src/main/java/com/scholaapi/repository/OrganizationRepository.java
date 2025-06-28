package com.scholaapi.repository;

import com.scholaapi.model.Organization;
import com.scholaapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    
    // Find organizations created by a specific user
    List<Organization> findByUserUuid(UUID userUuid);
    List<Organization> findByUser(User user);
} 