package com.scholaapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CourseRequest {

    @NotNull(message = "Organization UUID is required")
    private UUID organizationUuid;

    @NotNull(message = "Professor UUID is required")
    private UUID professorUuid;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Description is required")
    private String description;
} 