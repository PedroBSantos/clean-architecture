package com.bank.transfer.commands;

import com.bank.transfer.enums.EDocument;
import com.bank.transfer.enums.ETransaction;

public class TransferCommand extends Command {

    private EDocument documentAccountFromType;
    private String documentAccountFrom;
    private EDocument documentAccountToType;
    private String documentAccountTo;
    private float amount;

    public TransferCommand(String documentAccountFrom, EDocument documentAccountFromType,
            String documentAccountTo, EDocument documentAccountToType, float amount) {
        super(ETransaction.TRANSFER);
        this.documentAccountFromType = documentAccountFromType;
        this.documentAccountFrom = documentAccountFrom;
        this.documentAccountToType = documentAccountToType;
        this.documentAccountTo = documentAccountTo;
        this.amount = amount;
    }

    public String getDocumentAccountFrom() {
        return documentAccountFrom;
    }

    public String getDocumentAccountTo() {
        return documentAccountTo;
    }

    public float getAmount() {
        return amount;
    }

    public EDocument getDocumentAccountFromType() {
        return documentAccountFromType;
    }

    public EDocument getDocumentAccountToType() {
        return documentAccountToType;
    }
}
