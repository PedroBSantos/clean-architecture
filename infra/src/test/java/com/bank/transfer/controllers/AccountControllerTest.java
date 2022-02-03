package com.bank.transfer.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bank.transfer.enums.EDocument;
import com.bank.transfer.models.CreateAccountModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AccountControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int serverPort;

    @Test
    @DisplayName("Deve criar uma conta")
    public void test1() {
        var response = this.testRestTemplate.postForEntity("/accounts",
                new CreateAccountModel("Santander", "003", "123.123.123-12", EDocument.CPF),
                CreateAccountModel.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        var responseBody = response.getBody();
        assertEquals("123.123.123-12", responseBody.getDocumentNumber());
    }
}
