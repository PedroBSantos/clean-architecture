package com.bank.transfer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.validation.Valid;

import com.bank.transfer.models.CreateAccountModel;
import com.bank.transfer.models.CreditAccountModel;
import com.bank.transfer.models.GetAccountModel;
import com.bank.transfer.models.GetAccountRequest;
import com.bank.transfer.services.AccountApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping(value = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Accounts", description = "AccountController")
public class AccountController {

    private AccountApplicationService accountApplicationService;

    public AccountController(@Autowired AccountApplicationService accountApplicationService) {
        this.accountApplicationService = accountApplicationService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    @Transactional(readOnly = false)
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation(value = "Cria uma nova conta")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Recurso criado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição mal formatada"),
            @ApiResponse(code = 409, message = "Recurso já existe"),
            @ApiResponse(code = 500, message = "Exceção gerada") })
    public ResponseEntity<CreateAccountModel> createAccount(@RequestBody @Valid CreateAccountModel createAccountModel) {
        var uuid = this.accountApplicationService.createAccount(createAccountModel.getBank(),
                createAccountModel.getBranch(),
                createAccountModel.getDocumentNumber(), createAccountModel.getDocumentType());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(uuid).toUri();
        return ResponseEntity.created(uri).body(createAccountModel);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping
    @Transactional(readOnly = true)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Obtem uma conta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recurso encontrado"),
            @ApiResponse(code = 400, message = "Requisição mal formatada"),
            @ApiResponse(code = 404, message = "Recurso não encontrado"),
            @ApiResponse(code = 500, message = "Exceção gerada") })
    public ResponseEntity<GetAccountModel> getAccount(@RequestBody @Valid GetAccountRequest getAccountRequest) {
        var account = this.accountApplicationService.getAccount(getAccountRequest.getDocumentNumber(),
                getAccountRequest.getDocumentType());
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping(value = "/credit")
    @Transactional(readOnly = false)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Creditar em uma conta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operação concluida com sucesso"),
            @ApiResponse(code = 400, message = "Requisição mal formatada"),
            @ApiResponse(code = 404, message = "Recurso não encontrado"),
            @ApiResponse(code = 500, message = "Exceção gerada") })
    public ResponseEntity<?> creditAccount(@RequestBody @Valid CreditAccountModel creditAccountModel) {
        this.accountApplicationService.creditAccount(creditAccountModel.getDocumentType(),
                creditAccountModel.getDocumentNumber(), creditAccountModel.getAmount());
        return ResponseEntity.ok().build();
    }
}
