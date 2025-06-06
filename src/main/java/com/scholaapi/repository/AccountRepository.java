package com.scholaapi.repository;


import com.scholaapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByProviderId(String providerId);
}