package com.bank.transfer.valueobjects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.bank.transfer.enums.EDocument;

@Embeddable
public class Document {

    @Column(unique = true, nullable = false)
    private String documentNumber;
    @Enumerated(EnumType.STRING)
    private EDocument documentType;

    protected Document() {
    }

    public Document(String documentNumber, EDocument documentType) {
        this.documentNumber = documentNumber;
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public EDocument getDocumentType() {
        return documentType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((documentType == null) ? 0 : documentType.hashCode());
        result = prime * result + ((documentNumber == null) ? 0 : documentNumber.hashCode());
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
        Document other = (Document) obj;
        if (documentType != other.documentType)
            return false;
        if (documentNumber == null) {
            if (other.documentNumber != null)
                return false;
        } else if (!documentNumber.equals(other.documentNumber))
            return false;
        return true;
    }
}
