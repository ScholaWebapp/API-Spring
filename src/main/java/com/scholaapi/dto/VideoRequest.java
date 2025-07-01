package com.scholaapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class VideoRequest {
    private UUID moduleUuid;
    private String title;
    private String description;
    private String duration; // Optional - can be null. Format: "15:30" or "1:25:30"
} 