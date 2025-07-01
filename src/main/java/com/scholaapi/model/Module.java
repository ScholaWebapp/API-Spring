package com.scholaapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "modules")
@Getter
@Setter
public class Module {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID uuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "course_uuid", referencedColumnName = "uuid")
    private Course course;
    
    // Getter for course UUID only
    public UUID getCourseUuid() {
        return course != null ? course.getUuid() : null;
    }

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Unified resources with mixed ordering
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    private List<Resource> resources = new ArrayList<>();
} 