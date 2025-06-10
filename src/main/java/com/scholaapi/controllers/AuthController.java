package com.scholaapi.controllers;

import com.scholaapi.dto.RegisterRequest;
import com.scholaapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
//            @Valid
            @RequestBody
            RegisterRequest request
    ) {



        System.out.println("Received registration request:");
        System.out.println("First Name: " + request.getFirstName());
        System.out.println("Last Name: " + request.getLastName());
        System.out.println("Email: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());


//        authService.register(request);


        return ResponseEntity.ok("Registration successful!");
    }
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Test successful!");
    }
}