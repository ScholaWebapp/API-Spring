package com.scholaapi.dto;

import lombok.Data;

@Data
//@AllArgsConstructor
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

//public class AuthResponse {
//    private String token;
//}