package com.scholaapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class DocumentRequest {
    private UUID moduleUuid;
    private String title;
    private String description;
    private String fileType; // PDF, DOC, DOCX, TXT
} 