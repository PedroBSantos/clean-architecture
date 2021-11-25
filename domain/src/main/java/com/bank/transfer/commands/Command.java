package com.bank.transfer.commands;

import com.bank.transfer.enums.ETransaction;

public abstract class Command {
    
    private ETransaction type;

    protected Command(ETransaction type) {
        this.type = type;
    }

    public ETransaction getType() {
        return type;
    }
}
