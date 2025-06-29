package com.scholaapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrganizationTestRequest {

    @NotNull(message = "User UUID is required")
    private UUID userUuid;
} 