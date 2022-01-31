package com.bank.transfer.models;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.bank.transfer.enums.EDocument;

public class CreateAccountModel implements Serializable {
    
    @NotBlank
    @NotEmpty
    private String bank;
    @NotBlank
    @NotEmpty
    private String branch;
    @NotBlank
    @NotEmpty
    private String documentNumber;
    private EDocument documentType;

    public CreateAccountModel() {
    }

    public CreateAccountModel(String bank, String branch, String documentNumber, EDocument documentType) {
        this.bank = bank;
        this.branch = branch;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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
}
