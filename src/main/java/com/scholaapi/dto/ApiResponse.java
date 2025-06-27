package com.scholaapi.dto;

import lombok.Setter;

import java.util.List;

@Setter

public class ApiResponse<T> {
    private String message;
    private boolean success;
    private T data;
    private List<ApiError> errors;

    // Constructors, Getters, Setters
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(String message, List<ApiError> errors) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrors(errors);
        return response;
    }
}