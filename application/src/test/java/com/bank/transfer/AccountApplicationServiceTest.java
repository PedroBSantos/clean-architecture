package com.bank.transfer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import com.bank.transfer.builders.AccountBuilder;
import com.bank.transfer.enums.EDocument;
import com.bank.transfer.models.GetAccountModel;
import com.bank.transfer.notifications.NotificationContext;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.services.AccountApplicationService;
import com.bank.transfer.valueobjects.Document;

import org.junit.Test;

public class AccountApplicationServiceTest {

    private AccountRepository accountRepository;

    public AccountApplicationServiceTest() {
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
    public void shouldCreateAccount() {
        var notificationContext = new NotificationContext();
        var accountApplicationService = new AccountApplicationService(this.accountRepository, notificationContext);
        var bank = "033";
        var branch = "3";
        var document = "797.515.680-99";
        var documentType = EDocument.CPF;
        accountApplicationService.createAccount(bank, branch, document, documentType);
        var account = this.accountRepository.find(new Document(document, documentType));
        assertTrue(account.isPresent());
        assertNotNull(account.get().getNumber());
        assertTrue(account.get().balance() == 0.0f);
    }

    @Test
    public void shouldGetAccount() {
        var notificationContext = new NotificationContext();
        var accountApplicationService = new AccountApplicationService(this.accountRepository, notificationContext);
        var document = "431.006.250-48";
        var documentType = EDocument.CPF;
        var account = accountApplicationService.getAccount(document, documentType);
        assertNotNull(account);
        assertNotNull(account.getAccountNumber());
        assertNotNull(account.getBranch());
        assertNotNull(account.getBank());
        assertTrue(notificationContext.getNotifications().isEmpty());
        assertTrue(account instanceof GetAccountModel);
    }

    @Test
    public void shouldNotGetAccount() {
        var notificationContext = new NotificationContext();
        var accountApplicationService = new AccountApplicationService(this.accountRepository, notificationContext);
        var document = "431.006.250-48";
        var documentType = EDocument.CNPJ;
        var account = accountApplicationService.getAccount(document, documentType);
        assertNotNull(account);
        assertNull(account.getAccountNumber());
        assertNull(account.getBranch());
        assertNull(account.getBank());
        assertTrue(!notificationContext.getNotifications().isEmpty());
        assertTrue(notificationContext.getNotifications().size() == 1);
        assertTrue(account instanceof GetAccountModel);
    }

    @Test
    public void shouldCreditAccount() {
        var notificationContext = new NotificationContext();
        var accountApplicationService = new AccountApplicationService(this.accountRepository, notificationContext);
        var document = "431.006.250-48";
        var documentType = EDocument.CPF;
        var account = this.accountRepository.find(new Document(document, documentType)).get();
        assertTrue(account.balance() == 0.0f);
        accountApplicationService.creditAccount(documentType, document, 1000.0f);
        assertTrue(account.balance() == 1000.0f);
        assertTrue(notificationContext.getNotifications().isEmpty());
        assertTrue(notificationContext.getNotifications().size() == 0);
    }

    @Test
    public void shouldDebitAccount() {
        var notificationContext = new NotificationContext();
        var accountApplicationService = new AccountApplicationService(this.accountRepository, notificationContext);
        var document = "431.006.250-48";
        var documentType = EDocument.CPF;
        var account = this.accountRepository.find(new Document(document, documentType)).get();
        assertTrue(account.balance() == 0.0f);
        accountApplicationService.creditAccount(documentType, document, 1000.0f);
        assertTrue(account.balance() == 1000.0f);
        assertTrue(notificationContext.getNotifications().isEmpty());
        assertTrue(notificationContext.getNotifications().size() == 0);
        accountApplicationService.debitAccount(documentType, document, 1000.0f);
        assertTrue(account.balance() == 0.0f);
        assertTrue(notificationContext.getNotifications().isEmpty());
        assertTrue(notificationContext.getNotifications().size() == 0);
    }

    @Test
    public void shouldNotDebitAccountBalanceInsufficient() {
        var notificationContext = new NotificationContext();
        var accountApplicationService = new AccountApplicationService(this.accountRepository, notificationContext);
        var document = "431.006.250-48";
        var documentType = EDocument.CPF;
        var account = this.accountRepository.find(new Document(document, documentType)).get();
        assertTrue(account.balance() == 0.0f);
        accountApplicationService.debitAccount(documentType, document, 1000.0f);
        assertTrue(account.balance() == 0.0f);
        assertTrue(!notificationContext.getNotifications().isEmpty());
        assertTrue(notificationContext.getNotifications().size() == 1);
    }

    @Test
    public void shouldTransferAccounts() {
        var notificationContext = new NotificationContext();
        var accountApplicationService = new AccountApplicationService(this.accountRepository, notificationContext);
        var document1 = "431.006.250-48";
        var document2 = "362.184.830-45";
        var documentType1 = EDocument.CPF;
        var documentType2 = EDocument.CPF;
        var account1 = this.accountRepository.find(new Document(document1, documentType1)).get();
        var account2 = this.accountRepository.find(new Document(document2, documentType2)).get();
        assertTrue(account1.balance() == 0.0f);
        assertTrue(account2.balance() == 0.0f);
        accountApplicationService.creditAccount(documentType1, document1, 1000.0f);
        assertTrue(account1.balance() == 1000.0f);
        assertTrue(account2.balance() == 0.0f);
        assertTrue(notificationContext.getNotifications().isEmpty());
        assertTrue(notificationContext.getNotifications().size() == 0);
        accountApplicationService.transferAccounts(documentType1, document1, documentType2, document2, 1000.0f);
        assertTrue(account1.balance() == 0.0f);
        assertTrue(account2.balance() == 1000.0f);
        assertTrue(notificationContext.getNotifications().isEmpty());
        assertTrue(notificationContext.getNotifications().size() == 0);
    }

    @Test
    public void shouldNotTransferAccountsInsufficientAmount() {
        var notificationContext = new NotificationContext();
        var accountApplicationService = new AccountApplicationService(this.accountRepository, notificationContext);
        var document1 = "431.006.250-48";
        var document2 = "362.184.830-45";
        var documentType1 = EDocument.CPF;
        var documentType2 = EDocument.CPF;
        var account1 = this.accountRepository.find(new Document(document1, documentType1)).get();
        var account2 = this.accountRepository.find(new Document(document2, documentType2)).get();
        assertTrue(account1.balance() == 0.0f);
        assertTrue(account2.balance() == 0.0f);
        accountApplicationService.transferAccounts(documentType1, document1, documentType2, document2, 1000.0f);
        assertTrue(account1.balance() == 0.0f);
        assertTrue(account2.balance() == 0.0f);
        assertTrue(!notificationContext.getNotifications().isEmpty());
        assertTrue(notificationContext.getNotifications().size() == 1);
    }
}
