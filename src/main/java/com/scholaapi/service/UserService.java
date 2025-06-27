package com.scholaapi.service;

import com.scholaapi.model.User;
import com.scholaapi.repository.AccountRepository;
import com.scholaapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;



    public List<User> fetchAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void delete(UUID uuid) {
        accountRepository.deleteByUserUuid(uuid);
        userRepository.deleteByUuid(uuid);
    }



}
