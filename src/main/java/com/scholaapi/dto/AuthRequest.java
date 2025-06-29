package com.scholaapi.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AuthRequest {
    private String email;
    private String password;
}
