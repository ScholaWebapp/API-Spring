package com.scholaapi.repository;

import com.scholaapi.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {
    List<Module> findByCourse_UuidOrderByOrderIndexAsc(UUID courseUuid);
    
    // Delete modules by course UUID
    @Modifying
    @Query("DELETE FROM Module m WHERE m.course.uuid = :courseUuid")
    void deleteByCourseUuid(@Param("courseUuid") UUID courseUuid);
}
