package com.bank.transfer.services;

import java.util.UUID;

import com.bank.transfer.builders.AccountBuilder;
import com.bank.transfer.commands.CreditCommand;
import com.bank.transfer.commands.DebitCommand;
import com.bank.transfer.commands.TransferCommand;
import com.bank.transfer.context.NotificationContext;
import com.bank.transfer.enums.EDocument;
import com.bank.transfer.enums.ENotification;
import com.bank.transfer.handlers.CreditHandler;
import com.bank.transfer.handlers.DebitHandler;
import com.bank.transfer.handlers.TransferHandler;
import com.bank.transfer.models.GetAccountModel;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.valueobjects.Document;

public class AccountApplicationService {

    private AccountRepository accountRepository;
    private NotificationContext notificationContext;

    public AccountApplicationService(AccountRepository accountRepository, NotificationContext notificationContext) {
        this.accountRepository = accountRepository;
        this.notificationContext = notificationContext;
    }

    public void createAccount(String bank, String branch, String document, EDocument documentType) {
        var account = new AccountBuilder(new Document(document, documentType)).withBank(bank).withBranch(branch)
                .withNumber(UUID.randomUUID()).build();
        this.accountRepository.save(account);
    }

    public GetAccountModel getAccount(String documentNumber, EDocument documentType) {
        var document = new Document(documentNumber, documentType);
        var account = this.accountRepository.find(document);
        var serviceReturn = new GetAccountModel();
        account.ifPresentOrElse(a -> {
            serviceReturn.setAccountNumber(a.getNumber());
            serviceReturn.setBank(a.getBank());
            serviceReturn.setBranch(a.getBranch());
            serviceReturn.setBalance(a.balance());
        }, () -> this.notificationContext.add("Document not found: " + document.getNumber(), ENotification.NOT_EXISTS));
        return serviceReturn;
    }

    public void creditAccount(EDocument documentType, String documentNumber, float amount) {
        var creditCommand = new CreditCommand(documentType, documentNumber, amount);
        var creditCommandHandler = new CreditHandler(this.accountRepository, this.notificationContext);
        creditCommandHandler.handler(creditCommand);
    }

    public void debitAccount(EDocument documentType, String documentNumber, float amount) {
        var debitCommand = new DebitCommand(documentType, documentNumber, amount);
        var debitCommandHandler = new DebitHandler(this.accountRepository, this.notificationContext);
        debitCommandHandler.handler(debitCommand);
    }

    public void transferAccounts(EDocument documentAccountFromType, String documentAccountFrom,
            EDocument documentAccountToType, String documentAccountTo, float amount) {
        var transferCommand = new TransferCommand(documentAccountFrom, documentAccountFromType, documentAccountTo,
                documentAccountToType, amount);
        var transferCommandHandler = new TransferHandler(accountRepository, notificationContext);
        transferCommandHandler.handler(transferCommand);
    }
}
