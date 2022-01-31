package com.bank.transfer.models;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.bank.transfer.enums.EDocument;

public class GetAccountRequest implements Serializable {
    
    @NotBlank
    @NotEmpty
    private String documentNumber;
    private EDocument documentType;

    public GetAccountRequest() {
    }

    public GetAccountRequest(String documentNumber, EDocument documentType) {
        this.documentNumber = documentNumber;
        this.documentType = documentType;
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
