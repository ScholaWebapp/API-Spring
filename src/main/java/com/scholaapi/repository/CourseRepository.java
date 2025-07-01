package com.scholaapi.repository;

import com.scholaapi.model.Course;
import com.scholaapi.model.Organization;
import com.scholaapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    
    // Find courses by organization
    List<Course> findByOrganizationUuid(UUID organizationUuid);
    List<Course> findByOrganization(Organization organization);
    
    // Find courses by professor
    List<Course> findByProfessorUuid(UUID professorUuid);
    List<Course> findByProfessor(User professor);
    
    // Find courses by category
    List<Course> findByCategory(String category);
    
    // Find courses by title (case-insensitive search)
    List<Course> findByTitleContainingIgnoreCase(String title);
    
    // Find all courses except those in a specific organization
    @Query("SELECT c FROM Course c WHERE c.organization.uuid != :organizationUuid")
    List<Course> findAllExceptByOrganizationUuid(@Param("organizationUuid") UUID organizationUuid);
    
    // Delete courses by organization UUID
    @Modifying
    @Query("DELETE FROM Course c WHERE c.organization.uuid = :organizationUuid")
    void deleteByOrganizationUuid(@Param("organizationUuid") UUID organizationUuid);
    
    // Delete courses by professor UUID
    @Modifying
    @Query("DELETE FROM Course c WHERE c.professor.uuid = :professorUuid")
    void deleteByProfessorUuid(@Param("professorUuid") UUID professorUuid);
} 