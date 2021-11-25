package com.bank.transfer.valueobjects;

import com.bank.transfer.enums.EDocument;

public class Document {

    private String number;
    private EDocument documentType;

    public Document(String number, EDocument documentType) {
        this.number = number;
        this.documentType = documentType;
    }

    public String getNumber() {
        return number;
    }

    public EDocument getDocumentType() {
        return documentType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((documentType == null) ? 0 : documentType.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
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
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        return true;
    }
}
