package com.bank.transfer.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bank.transfer.enums.ETransaction;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "id_account")
    private Account account;
    @Enumerated(EnumType.STRING)
    private ETransaction type;
    private float amount;

    public Transaction() {
    }

    public Transaction(Account account, ETransaction type, float amount) {
        this.id = UUID.randomUUID();
        this.account = account;
        this.type = type;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public ETransaction getType() {
        return type;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((account == null) ? 0 : account.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Transaction other = (Transaction) obj;
        if (account == null) {
            if (other.account != null)
                return false;
        } else if (!account.equals(other.account))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
