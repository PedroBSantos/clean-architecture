package com.bank.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bank.transfer.entities.Account;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.valueobjects.Document;

public class AccountRepositoryFake implements AccountRepository {

    private List<Account> accounts;

    public AccountRepositoryFake() {
        this.accounts = new ArrayList<>();
    }

    @Override
    public void save(Account account) {
        var accountExistent = this.accounts.stream().filter(a -> a.equals(account)).findFirst();
        accountExistent.ifPresentOrElse(a -> {
            this.accounts.remove(a);
        }, () -> this.accounts.add(account));
    }

    @Override
    public Optional<Account> find(Document document) {
        var optionalAccount = this.accounts.stream().filter(account -> document.equals(account.getDocument()))
                .findFirst();
        return optionalAccount;
    }
}
