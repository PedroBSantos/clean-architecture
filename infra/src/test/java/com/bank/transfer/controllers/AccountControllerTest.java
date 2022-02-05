package com.bank.transfer.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bank.transfer.enums.EDocument;
import com.bank.transfer.models.CreateAccountModel;
import com.bank.transfer.models.CreditAccountModel;
import com.bank.transfer.models.DebitAccountModel;
import com.bank.transfer.models.GetAccountModel;
import com.bank.transfer.models.TransferAccountModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AccountControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int serverPort;

    @Test
    @DisplayName("Must create account")
    public void test1() {
        var authHost = "http://localhost:8000/auth/realms/dev-realm/protocol/openid-connect/token";
        var keycloakAccessToken = KeycloakApi.getKeycloakAccessToken(authHost, "bank-app", "password", "user@email.com",
                "123456");
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + keycloakAccessToken);
        var request = new HttpEntity<CreateAccountModel>(
                new CreateAccountModel("Santander", "003", "123.123.123-12", EDocument.CPF), headers);
        var response = this.testRestTemplate.postForEntity("/accounts", request, GetAccountModel.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Must get account")
    public void test2() {
        var authHost = "http://localhost:8000/auth/realms/dev-realm/protocol/openid-connect/token";
        var keycloakAccessToken = KeycloakApi.getKeycloakAccessToken(authHost, "bank-app", "password", "user@email.com",
                "123456");
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + keycloakAccessToken);
        var request = new HttpEntity<GetAccountModel>(headers);
        var response = this.testRestTemplate.exchange("/accounts/123.123.123-12/CPF", HttpMethod.GET, request,
                GetAccountModel.class, 1);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0f, response.getBody().getBalance());
    }

    @Test
    @DisplayName("Must credit account")
    public void test3() {
        var authHost = "http://localhost:8000/auth/realms/dev-realm/protocol/openid-connect/token";
        var keycloakAccessToken = KeycloakApi.getKeycloakAccessToken(authHost, "bank-app", "password", "user@email.com",
                "123456");
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + keycloakAccessToken);
        var request = new HttpEntity<CreditAccountModel>(
                new CreditAccountModel("123.123.123-12", EDocument.CPF, 100.0f), headers);
        var response = this.testRestTemplate.postForEntity("/accounts/credit", request, CreditAccountModel.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Must debit account")
    public void test4() {
        var authHost = "http://localhost:8000/auth/realms/dev-realm/protocol/openid-connect/token";
        var keycloakAccessToken = KeycloakApi.getKeycloakAccessToken(authHost, "bank-app", "password", "user@email.com",
                "123456");
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + keycloakAccessToken);
        var request = new HttpEntity<DebitAccountModel>(
                new DebitAccountModel("123.123.123-12", EDocument.CPF, 90.0f), headers);
        var response = this.testRestTemplate.postForEntity("/accounts/debit", request, DebitAccountModel.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Must transfer accounts")
    public void test5() {
        var authHost = "http://localhost:8000/auth/realms/dev-realm/protocol/openid-connect/token";
        var keycloakAccessToken = KeycloakApi.getKeycloakAccessToken(authHost, "bank-app", "password", "user@email.com",
                "123456");
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + keycloakAccessToken);
        var createAccountRequest = new HttpEntity<CreateAccountModel>(
                new CreateAccountModel("Santander", "003", "222.222.222-12", EDocument.CPF), headers);
        var createAccountResponse = this.testRestTemplate.postForEntity("/accounts", createAccountRequest,
                GetAccountModel.class);
        assertTrue(createAccountResponse.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatus.CREATED, createAccountResponse.getStatusCode());
        var transferAccountsRequest = new HttpEntity<TransferAccountModel>(
                new TransferAccountModel("123.123.123-12", EDocument.CPF, "222.222.222-12", EDocument.CPF, 5.0f),
                headers);
        var transferAccountsResponse = this.testRestTemplate.postForEntity("/accounts/transfer",
                transferAccountsRequest, TransferAccountModel.class);
        assertTrue(transferAccountsResponse.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatus.OK, transferAccountsResponse.getStatusCode());
    }
}
