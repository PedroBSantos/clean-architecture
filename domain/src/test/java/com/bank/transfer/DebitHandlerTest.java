package com.bank.transfer;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import com.bank.transfer.builders.AccountBuilder;
import com.bank.transfer.commands.CreditCommand;
import com.bank.transfer.commands.DebitCommand;
import com.bank.transfer.context.NotificationContext;
import com.bank.transfer.enums.EDocument;
import com.bank.transfer.enums.ENotification;
import com.bank.transfer.handlers.CreditHandler;
import com.bank.transfer.handlers.DebitHandler;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.valueobjects.Document;

import org.junit.Test;

public class DebitHandlerTest {

    private AccountRepository accountRepository;

    public DebitHandlerTest() {
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
        this.accountRepository.create(account1);
        this.accountRepository.create(account2);
    }

    @Test
    public void shouldDebitAccount() {
        var notificationContext = new NotificationContext();
        var document = new Document("431.006.250-48", EDocument.CPF);
        var account = this.accountRepository.find(document);
        var creditCommand = new CreditCommand(EDocument.CPF, "431.006.250-48", 1000);
        var creditHandler = new CreditHandler(this.accountRepository, notificationContext);
        assertTrue(account.get().balance() == 0.0f);
        creditHandler.handler(creditCommand);
        assertTrue(account.get().balance() == 1000.0f);
        var debitCommand = new DebitCommand(EDocument.CPF, "431.006.250-48", 1000.0f);
        var debitHandler = new DebitHandler(accountRepository, notificationContext);
        debitHandler.handler(debitCommand);
        assertTrue(account.get().balance() == 0.0f);
        assertTrue(notificationContext.getNotifications().isEmpty());
    }

    @Test
    public void shouldNotDebitAccount() {
        var notificationContext = new NotificationContext();
        var debitCommand = new DebitCommand(EDocument.CPF, "431.006.250-48", 1000.0f);
        var debitHandler = new DebitHandler(accountRepository, notificationContext);
        debitHandler.handler(debitCommand);
        assertTrue(!notificationContext.getNotifications().isEmpty());
        assertTrue(notificationContext.getNotifications().stream()
                .anyMatch(notification -> notification.getNotificationType() == ENotification.VALIDATION));
    }
}
