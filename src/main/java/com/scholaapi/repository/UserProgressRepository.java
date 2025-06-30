package com.scholaapi.repository;

import com.scholaapi.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, UUID> {
    List<UserProgress> findByUserUuid(UUID userUuid);
    Optional<UserProgress> findByUserUuidAndCourseUuid(UUID userUuid, UUID courseUuid);
    List<UserProgress> findByCourseUuid(UUID courseUuid);
} 