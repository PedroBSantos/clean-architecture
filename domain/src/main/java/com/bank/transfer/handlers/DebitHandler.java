package com.bank.transfer.handlers;

import com.bank.transfer.commands.DebitCommand;
import com.bank.transfer.notifications.ENotification;
import com.bank.transfer.notifications.NotificationContext;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.valueobjects.Document;

public class DebitHandler {

    private AccountRepository accountRepository;
    private NotificationContext notificationContext;

    public DebitHandler(AccountRepository accountRepository, NotificationContext notificationContext) {
        this.accountRepository = accountRepository;
        this.notificationContext = notificationContext;
    }

    public void handler(DebitCommand debitCommand) {
        var document = new Document(debitCommand.getDocument(), debitCommand.getDocumentType());
        var account = this.accountRepository.find(document);
        account.ifPresentOrElse(a -> {
            if (!a.debit(debitCommand.getAmount())) {
                this.notificationContext.add("Account balance insufficent or invalid debit amount",
                        ENotification.VALIDATION);
                return;
            }
            this.accountRepository.create(a);
        }, () -> this.notificationContext.add("Document not found: " + document.getDocumentNumber(),
                ENotification.NOT_EXISTS));
    }
}
