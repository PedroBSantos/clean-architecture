package com.bank.transfer;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import com.bank.transfer.builders.AccountBuilder;
import com.bank.transfer.enums.EDocument;
import com.bank.transfer.valueobjects.Document;

import org.junit.Test;

public class AccountTest {

    @Test
    public void shouldMakeCreditInAccount() {
        var idAccount = UUID.randomUUID();
        var account = new AccountBuilder(new Document("431.006.250-48", EDocument.CPF)).withBank("033").withBranch("3")
                .withNumber(idAccount).build();
        assertTrue(account.getBank().equals("033"));
        assertTrue(account.getBranch().equals("3"));
        assertTrue(account.getNumber().equals(idAccount));
        assertTrue(account.balance() == 0.0f);
        account.credit(1000.0f);
        assertTrue(account.balance() == 1000.0f);
    }

    @Test
    public void shouldMakeDebitInAccount() {
        var idAccount = UUID.randomUUID();
        var account = new AccountBuilder(new Document("431.006.250-48", EDocument.CPF)).withBank("033").withBranch("3")
                .withNumber(idAccount).build();
        assertTrue(account.getBank().equals("033"));
        assertTrue(account.getBranch().equals("3"));
        assertTrue(account.getNumber().equals(idAccount));
        assertTrue(account.getDocument().getDocumentNumber().equals("431.006.250-48"));
        assertTrue(account.getDocument().getDocumentType() == EDocument.CPF);
        assertTrue(account.balance() == 0.0f);
        account.credit(1000.0f);
        assertTrue(account.balance() == 1000.0f);
        account.debit(1000.0f);
        assertTrue(account.balance() == 0.0f);
    }
}
