package com.scholaapi.controllers;

import com.scholaapi.repository.UserRepository;
import com.scholaapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {



    @Autowired
    private UserService userService;


    public UserController( UserService userService ) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<?>  getAllUser() {
        return ResponseEntity.ok(userService.fetchAll());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        userService.delete(uuid);
        return ResponseEntity.noContent().build();
    }


}
