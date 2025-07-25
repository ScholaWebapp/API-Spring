package com.scholaapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ModuleRequest {
    private UUID courseUuid;
    private String title;
    private String description;
} 