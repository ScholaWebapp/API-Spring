package com.scholaapi.controllers;

import com.scholaapi.dto.RegisterRequest;
import com.scholaapi.model.Account;
import com.scholaapi.model.AuthRequest;
import com.scholaapi.model.AuthResponse;
import com.scholaapi.model.User;
import com.scholaapi.repository.AccountRepository;
import com.scholaapi.repository.UserRepository;
import com.scholaapi.service.AuthService;
import com.scholaapi.service.JwtService;
import com.scholaapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;




    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        User user = userOpt.get();


        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));


        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }


        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }



    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody
            RegisterRequest request
    ) {
        System.out.println("Received registration request:");
        System.out.println("First Name: " + request.getFirstName());
        System.out.println("Last Name: " + request.getLastName());
        System.out.println("Email: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());

        authService.register(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Registration successful!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<String> verifyTokenAuthenticity(@RequestHeader("Authorization") String authHeader) {
        // Strip "Bearer " prefix
        String token = authHeader.replace("Bearer ", "");

        // Optionally extract user info if needed
        boolean isValid = jwtService.isTokenValid(token);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        return ResponseEntity.ok("Token is valid 🎉");
    }


    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Test successful!");
    }
}