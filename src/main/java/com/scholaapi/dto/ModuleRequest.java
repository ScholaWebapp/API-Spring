package com.scholaapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleRequest {
    private String title;
    private String description;
    private Integer orderIndex;
} 