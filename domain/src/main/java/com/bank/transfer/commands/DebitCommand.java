package com.bank.transfer.commands;

import com.bank.transfer.enums.EDocument;
import com.bank.transfer.enums.ETransaction;

public class DebitCommand extends Command {

    private EDocument documentType;
    private String document;
    private float amount;

    public DebitCommand(EDocument documentType, String document, float amount) {
        super(ETransaction.DEBIT);
        this.documentType = documentType;
        this.document = document;
        this.amount = amount;
    }

    public String getDocument() {
        return document;
    }

    public float getAmount() {
        return amount;
    }

    public EDocument getDocumentType() {
        return documentType;
    }
}
