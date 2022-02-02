package com.bank.transfer.models;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.bank.transfer.enums.EDocument;

public class CreditAccountModel implements Serializable {
    
    @NotBlank
    @NotEmpty
    private String documentNumber;
    private EDocument documentType;
    private float amount;

    public CreditAccountModel() {
    }

    public CreditAccountModel(@NotBlank @NotEmpty String documentNumber, EDocument documentType, float amount) {
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.amount = amount;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public EDocument getDocumentType() {
        return documentType;
    }

    public void setDocumentType(EDocument documentType) {
        this.documentType = documentType;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
