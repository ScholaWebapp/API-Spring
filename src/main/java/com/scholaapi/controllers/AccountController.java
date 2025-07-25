package com.scholaapi.controllers;

import com.scholaapi.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/role/{uuid}")
    public ResponseEntity<Optional<String>> getUserRole(@PathVariable UUID uuid){
        Optional<String> role = accountService.getAccountRole(uuid);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/{uuid}/ban")
    public ResponseEntity<?> banUser(@PathVariable UUID uuid) {
        accountService.banUser(uuid);
        return ResponseEntity.ok("User banned.");
    }

    @PostMapping("/{uuid}/unban")
    public ResponseEntity<?> unbanUser(@PathVariable UUID uuid) {
        accountService.unbanUser(uuid);
        return ResponseEntity.ok("User unbanned.");
    }
}