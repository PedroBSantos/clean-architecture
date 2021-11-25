package com.bank.transfer;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import com.bank.transfer.builders.AccountBuilder;
import com.bank.transfer.commands.CreditCommand;
import com.bank.transfer.context.NotificationContext;
import com.bank.transfer.enums.EDocument;
import com.bank.transfer.handlers.CreditHandler;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.valueobjects.Document;

import org.junit.Test;

public class CreditHandlerTest {

    private AccountRepository accountRepository;

    public CreditHandlerTest() {
        this.accountRepository = new AccountRepositoryFake();
        this.mock();
    }

    private void mock() {
        var idAccount1 = UUID.randomUUID();
        var idAccount2 = UUID.randomUUID();
        var account1 = new AccountBuilder(new Document("431.006.250-48", EDocument.CPF)).withBank("033").withBranch("3")
                .withNumber(idAccount1).build();
        var account2 = new AccountBuilder(new Document("362.184.830-45", EDocument.CPF)).withBank("033").withBranch("8")
                .withNumber(idAccount2).build();
        this.accountRepository.save(account1);
        this.accountRepository.save(account2);
    }

    @Test
    public void shouldCreditAccount() {
        var notificationContext = new NotificationContext();
        var document = new Document("431.006.250-48", EDocument.CPF);
        var account = this.accountRepository.find(document);
        var creditCommand = new CreditCommand(EDocument.CPF, "431.006.250-48", 1000.0f);
        assertTrue(creditCommand.getDocumentType() == EDocument.CPF);
        assertTrue(creditCommand.getDocument().equals("431.006.250-48"));
        assertTrue(creditCommand.getAmount() == 1000.0f);
        var creditHandler = new CreditHandler(this.accountRepository, notificationContext);
        assertTrue(account.get().balance() == 0.0f);
        creditHandler.handler(creditCommand);
        assertTrue(account.get().balance() == 1000.0f);
        assertTrue(notificationContext.getNotifications().isEmpty());
    }
}
