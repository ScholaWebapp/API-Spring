package com.scholaapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
public class Document {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "file_type", nullable = false)
    private String fileType; // PDF, DOC, etc.

    @Column(name = "file_size")
    private Long fileSize;


    @Column(name = "file_url")
    private String fileUrl;

    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
} 