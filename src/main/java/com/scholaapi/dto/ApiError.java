package com.scholaapi.dto;

public class ApiError {
    private String field;
    private String message;

    public ApiError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}