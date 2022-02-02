package com.bank.transfer;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import com.bank.transfer.builders.AccountBuilder;
import com.bank.transfer.commands.TransferCommand;
import com.bank.transfer.context.NotificationContext;
import com.bank.transfer.enums.EDocument;
import com.bank.transfer.enums.ENotification;
import com.bank.transfer.handlers.TransferHandler;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.valueobjects.Document;

import org.junit.Test;

public class TransferHandlerTest {

    private AccountRepository accountRepository;

    public TransferHandlerTest() {
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
    public void shouldTransferAmount() {
        var notificationContext = new NotificationContext();
        var from = this.accountRepository.find(new Document("431.006.250-48", EDocument.CPF));
        var to = this.accountRepository.find(new Document("362.184.830-45", EDocument.CPF));
        from.get().credit(1000.0f);
        var transferCommand = new TransferCommand("431.006.250-48", EDocument.CPF,
                "362.184.830-45", EDocument.CPF, 1000.0f);
        var transferHandler = new TransferHandler(accountRepository, notificationContext);
        assertTrue(from.get().balance() == 1000.0f);
        assertTrue(to.get().balance() == 0.0f);
        transferHandler.handler(transferCommand);
        assertTrue(from.get().balance() == 0.0f);
        assertTrue(to.get().balance() == 1000.0f);
        assertTrue(notificationContext.getNotifications().isEmpty());
    }

    @Test
    public void shouldNotTransferAmount() {
        var notificationContext = new NotificationContext();
        var transferCommand = new TransferCommand("431.006.250-48", EDocument.CPF,
                "362.184.830-45", EDocument.CPF, 1000.0f);
        var transferHandler = new TransferHandler(accountRepository, notificationContext);
        transferHandler.handler(transferCommand);
        assertTrue(notificationContext.getNotifications().stream()
                .anyMatch(notification -> notification.getNotificationType() == ENotification.VALIDATION));
        assertTrue(!notificationContext.getNotifications().isEmpty());
    }

    @Test
    public void shouldNotTransferAmountSameAccounts() {
        var notificationContext = new NotificationContext();
        var transferCommand = new TransferCommand("431.006.250-48", EDocument.CPF,
                "431.006.250-48", EDocument.CPF, 1000.0f);
        var transferHandler = new TransferHandler(accountRepository, notificationContext);
        transferHandler.handler(transferCommand);
        assertTrue(notificationContext.getNotifications().stream()
                .anyMatch(notification -> notification.getNotificationType() == ENotification.VALIDATION));
        assertTrue(!notificationContext.getNotifications().isEmpty());
    }
}
