package com.scholaapi.service;

// src/main/java/com/schola/service/AuthService.java

import com.scholaapi.dto.RegisterRequest;
import com.scholaapi.model.Account;
import com.scholaapi.model.User;
import com.scholaapi.repository.AccountRepository;
import com.scholaapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already taken");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        userRepository.save(user);

        Account account = new Account();
        account.setUser(user);
        account.setProviderId(request.getEmail());
        account.setPasswordHash(passwordEncoder.encode(request.getPassword())); // Replace with real hash later
        account.setRole("NORMAL");

        accountRepository.save(account);

    }
}