package com.scholaapi.service;

import com.scholaapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public void banUser(UUID uuid) {
        accountRepository.banByUserUuid(uuid);
    }

    @Transactional
    public void unbanUser(UUID uuid) {
        accountRepository.unbanByUserUuid(uuid);
    }

    public Optional<String> getAccountRole(UUID uuid) {
        return accountRepository.findRoleByUserUuid(uuid);
    }
}
