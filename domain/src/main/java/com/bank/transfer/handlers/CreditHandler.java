package com.bank.transfer.handlers;

import com.bank.transfer.commands.CreditCommand;
import com.bank.transfer.context.NotificationContext;
import com.bank.transfer.enums.ENotification;
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
        account.ifPresentOrElse(a -> a.credit(creditCommand.getAmount()), () -> this.notificationContext
                .add("Document not found: " + document.getNumber(), ENotification.NOT_EXISTS));
    }
}
