package com.scholaapi.repository;

import com.scholaapi.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {
    List<Resource> findByModuleUuidOrderByOrderIndexAsc(UUID moduleUuid);
    List<Resource> findByModuleUuidAndResourceTypeOrderByOrderIndexAsc(UUID moduleUuid, Resource.ResourceType resourceType);
    java.util.Optional<Resource> findByVideoUuid(UUID videoUuid);
    java.util.Optional<Resource> findByDocumentUuid(UUID documentUuid);
    java.util.Optional<Resource> findByQuizUuid(UUID quizUuid);
    
    // Delete resources by module UUID
    @Modifying
    @Query("DELETE FROM Resource r WHERE r.module.uuid = :moduleUuid")
    void deleteByModuleUuid(@Param("moduleUuid") UUID moduleUuid);
} 