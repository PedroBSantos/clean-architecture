package com.bank.transfer.handlers;

import com.bank.transfer.commands.TransferCommand;
import com.bank.transfer.context.NotificationContext;
import com.bank.transfer.enums.ENotification;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.valueobjects.Document;

public class TransferHandler {

    private AccountRepository accountRepository;
    private NotificationContext notificationContext;

    public TransferHandler(AccountRepository accountRepository, NotificationContext notificationContext) {
        this.accountRepository = accountRepository;
        this.notificationContext = notificationContext;
    }

    public void handler(TransferCommand transferCommand) {
        var documentAccountFrom = new Document(transferCommand.getDocumentAccountFrom(),
                transferCommand.getDocumentAccountFromType());
        var accountFrom = this.accountRepository.find(documentAccountFrom);
        if (!accountFrom.isPresent()) {
            this.notificationContext.add("Document not found: " + documentAccountFrom.getDocumentNumber(),
                    ENotification.NOT_EXISTS);
            return;
        }
        var documentAccountTo = new Document(transferCommand.getDocumentAccountTo(),
                transferCommand.getDocumentAccountToType());
        var accountTo = this.accountRepository.find(documentAccountTo);
        if (!accountTo.isPresent()) {
            this.notificationContext.add("Document not found: " + documentAccountTo.getDocumentNumber(),
                    ENotification.NOT_EXISTS);
            return;
        }
        if (accountFrom.equals(accountTo)) {
            this.notificationContext.add("You can not transfer to the same account",
                    ENotification.VALIDATION);
            return;
        }
        if (!accountFrom.get().debit(transferCommand.getAmount())) {
            this.notificationContext.add("Account: " + accountFrom.get().getNumber() + " balance insufficent",
                    ENotification.VALIDATION);
            return;
        }
        accountTo.get().credit(transferCommand.getAmount());
        this.accountRepository.create(accountFrom.get());
        this.accountRepository.create(accountTo.get());
    }
}
