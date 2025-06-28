package com.scholaapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID uuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid")
    private User user; // Creator/owner

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(columnDefinition = "UUID[]")
    private List<UUID> members = new ArrayList<>();
} 