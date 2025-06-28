package com.scholaapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "course_enrollments")
@Getter
@Setter
public class CourseEnrollment {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID uuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_uuid", referencedColumnName = "uuid")
    private Course course;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(nullable = false)
    private String status = "enrolled"; // enrolled, in_progress, completed, dropped

    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0; // 0-100

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt = LocalDateTime.now();
} 