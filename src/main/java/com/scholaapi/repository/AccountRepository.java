package com.scholaapi.repository;


import com.scholaapi.model.Account;
import com.scholaapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByProviderId(String providerId);
    Optional<Account> findByUser(User user);

    @Modifying
    @Query("DELETE FROM Account a WHERE a.user.uuid = :uuid")
    void deleteByUserUuid(@Param("uuid") UUID uuid);


    @Modifying
    @Query("UPDATE Account a SET a.role = 'BANNED' WHERE a.user.uuid = :uuid")
    void banByUserUuid(@Param("uuid") UUID uuid);


    @Modifying
    @Query("UPDATE Account a SET a.role = 'NORMAL' WHERE a.user.uuid = :uuid")
    void unbanByUserUuid(@Param("uuid") UUID uuid);


    @Query("SELECT a.role FROM Account a WHERE a.user.uuid  = :userUuid")
    Optional<String> findRoleByUserUuid(@Param("userUuid") UUID userUuid);


}