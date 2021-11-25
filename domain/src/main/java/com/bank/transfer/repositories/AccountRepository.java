package com.bank.transfer.repositories;

import java.util.Optional;

import com.bank.transfer.entities.Account;
import com.bank.transfer.valueobjects.Document;

public interface AccountRepository {
    
    void save(Account account);

    Optional<Account> find(Document document);
}
