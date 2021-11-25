package com.bank.transfer.entities;

import com.bank.transfer.enums.ETransaction;

public class Transaction {
    
    private ETransaction type;
    private float amount;
    
    public Transaction(ETransaction type, float amount) {
        this.type = type;
        this.amount = amount;
    }

    public ETransaction getType() {
        return type;
    }

    public float getAmount() {
        return amount;
    }
}
