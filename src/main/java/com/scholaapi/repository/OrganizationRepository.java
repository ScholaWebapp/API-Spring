package com.scholaapi.repository;

import com.scholaapi.model.Organization;
import com.scholaapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    
    // Find organizations created by a specific user
    List<Organization> findByUserUuid(UUID userUuid);
    List<Organization> findByUser(User user);
    
    // Find organizations where a user is a member
    @Query("SELECT DISTINCT o FROM Organization o JOIN o.members m WHERE m = :memberUuid")
    List<Organization> findByMemberUuid(@Param("memberUuid") UUID memberUuid);
    
    // Find organizations by name (case-insensitive)
    List<Organization> findByNameContainingIgnoreCase(String name);
    
    // Find all organizations EXCEPT those created by a specific user
    @Query("SELECT o FROM Organization o WHERE o.user.uuid != :userUuid")
    List<Organization> findAllExceptByUserUuid(@Param("userUuid") UUID userUuid);
    
    // Delete organizations by user UUID
    @Modifying
    @Query("DELETE FROM Organization o WHERE o.user.uuid = :userUuid")
    void deleteByUserUuid(@Param("userUuid") UUID userUuid);
} 