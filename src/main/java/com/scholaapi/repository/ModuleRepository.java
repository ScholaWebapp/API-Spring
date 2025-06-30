package com.scholaapi.repository;

import com.scholaapi.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {
    List<Module> findByCourseUuidOrderByOrderIndexAsc(UUID courseUuid);
} 