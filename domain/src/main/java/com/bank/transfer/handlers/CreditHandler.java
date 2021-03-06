package com.bank.transfer.handlers;

import com.bank.transfer.commands.CreditCommand;
import com.bank.transfer.notifications.ENotification;
import com.bank.transfer.notifications.NotificationContext;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.valueobjects.Document;

public class CreditHandler {

    private AccountRepository accountRepository;
    private NotificationContext notificationContext;

    public CreditHandler(AccountRepository accountRepository, NotificationContext notificationContext) {
        this.accountRepository = accountRepository;
        this.notificationContext = notificationContext;
    }

    public void handler(CreditCommand creditCommand) {
        var document = new Document(creditCommand.getDocument(), creditCommand.getDocumentType());
        var account = this.accountRepository.find(document);
        account.ifPresentOrElse(a -> {
            if (a.credit(creditCommand.getAmount())) {
                this.accountRepository.create(a);
                return;
            }
            this.notificationContext.add("Invalid amount: " + creditCommand.getAmount(),
                    ENotification.VALIDATION);
        }, () -> this.notificationContext.add("Document not found: " + document.getDocumentNumber(),
                ENotification.NOT_EXISTS));
    }
}
