package com.bank.transfer.repositories;

import java.util.Optional;
import java.util.UUID;

import com.bank.transfer.entities.Account;
import com.bank.transfer.valueobjects.Document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends AccountRepository, JpaRepository<Account, UUID> {

    Optional<Account> findByDocument(Document document);

    @Override
    default void create(Account account) {
        this.save(account);
    }

    @Override
    default Optional<Account> find(Document document) {
        return this.findByDocument(document);
    }
}
