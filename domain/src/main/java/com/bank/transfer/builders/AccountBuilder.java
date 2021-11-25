package com.bank.transfer.builders;

import com.bank.transfer.entities.Account;
import com.bank.transfer.valueobjects.Document;
import java.util.UUID;

public class AccountBuilder {

    private UUID number;
    private String bank;
    private String branch;
    private Document document;

    public AccountBuilder(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return this.document;
    }

    public String getBank() {
        return this.bank;
    }

    public AccountBuilder withBank(String bank) {
        this.bank = bank;
        return this;
    }

    public AccountBuilder withBranch(String branch) {
        this.branch = branch;
        return this;
    }

    public String getBranch() {
        return this.branch;
    }

    public AccountBuilder withNumber(UUID number) {
        this.number = number;
        return this;
    }

    public UUID getNumber() {
        return this.number;
    }

    public Account build() {
        return new Account(this);
    }
}
