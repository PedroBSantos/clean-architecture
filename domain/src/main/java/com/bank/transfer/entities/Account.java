package com.bank.transfer.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.bank.transfer.builders.AccountBuilder;
import com.bank.transfer.enums.ETransaction;
import com.bank.transfer.valueobjects.Document;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID number;
    private String bank;
    private String branch;
    @Embedded
    private Document document;
    @OneToMany(mappedBy = "account", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Transaction> transactions;

    protected Account() {
        this.transactions = new ArrayList<>();
    }

    public Account(AccountBuilder accountBuilder) {
        this();
        this.bank = accountBuilder.getBank();
        this.branch = accountBuilder.getBranch();
        this.number = accountBuilder.getNumber();
        this.document = accountBuilder.getDocument();
    }

    public String getBank() {
        return bank;
    }

    public String getBranch() {
        return branch;
    }

    public UUID getNumber() {
        return number;
    }

    public Document getDocument() {
        return document;
    }

    public List<Transaction> getTransactions() {
        return transactions.stream().collect(Collectors.toList());
    }

    public boolean credit(float amount) {
        if (amount < 0.0f) {
            return false;
        }
        this.transactions.add(new Transaction(this, ETransaction.CREDIT, amount));
        return true;
    }

    public boolean debit(float amount) {
        if (this.balance() < amount || amount <= 0.0f) {
            return false;
        }
        this.transactions.add(new Transaction(this, ETransaction.DEBIT, amount));
        return true;
    }

    public float balance() {
        float result = 0.0f;
        for (Transaction transaction : this.transactions) {
            if (transaction.getType() == ETransaction.CREDIT) {
                result += transaction.getAmount();
            }
            if (transaction.getType() == ETransaction.DEBIT) {
                result -= transaction.getAmount();
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bank == null) ? 0 : bank.hashCode());
        result = prime * result + ((branch == null) ? 0 : branch.hashCode());
        result = prime * result + ((document == null) ? 0 : document.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (bank == null) {
            if (other.bank != null)
                return false;
        } else if (!bank.equals(other.bank))
            return false;
        if (branch == null) {
            if (other.branch != null)
                return false;
        } else if (!branch.equals(other.branch))
            return false;
        if (document == null) {
            if (other.document != null)
                return false;
        } else if (!document.equals(other.document))
            return false;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        return true;
    }
}
