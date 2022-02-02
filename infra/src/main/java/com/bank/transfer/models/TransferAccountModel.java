package com.bank.transfer.models;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.bank.transfer.enums.EDocument;

public class TransferAccountModel implements Serializable {

    @NotBlank
    @NotEmpty
    private String documentNumberSource;
    private EDocument documentTypeSource;
    @NotBlank
    @NotEmpty
    private String documentNumberDestiny;
    private EDocument documentTypeDestiny;
    private float amount;

    public TransferAccountModel() {
    }

    public TransferAccountModel(@NotBlank @NotEmpty String documentNumberSource, EDocument documentTypeSource,
            @NotBlank @NotEmpty String documentNumberDestiny, EDocument documentTypeDestiny, float amount) {
        this.documentNumberSource = documentNumberSource;
        this.documentTypeSource = documentTypeSource;
        this.documentNumberDestiny = documentNumberDestiny;
        this.documentTypeDestiny = documentTypeDestiny;
        this.amount = amount;
    }

    public String getDocumentNumberSource() {
        return documentNumberSource;
    }

    public void setDocumentNumberSource(String documentNumberSource) {
        this.documentNumberSource = documentNumberSource;
    }

    public EDocument getDocumentTypeSource() {
        return documentTypeSource;
    }

    public void setDocumentTypeSource(EDocument documentTypeSource) {
        this.documentTypeSource = documentTypeSource;
    }

    public String getDocumentNumberDestiny() {
        return documentNumberDestiny;
    }

    public void setDocumentNumberDestiny(String documentNumberDestiny) {
        this.documentNumberDestiny = documentNumberDestiny;
    }

    public EDocument getDocumentTypeDestiny() {
        return documentTypeDestiny;
    }

    public void setDocumentTypeDestiny(EDocument documentTypeDestiny) {
        this.documentTypeDestiny = documentTypeDestiny;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}
