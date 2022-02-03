package com.bank.transfer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.bank.transfer.enums.EDocument;
import com.bank.transfer.models.*;
import com.bank.transfer.services.AccountApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping(value = "/accounts")
@Api(tags = "Accounts", description = "AccountController")
public class AccountController {

    private AccountApplicationService accountApplicationService;

    public AccountController(@Autowired AccountApplicationService accountApplicationService) {
        this.accountApplicationService = accountApplicationService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation(value = "Cria uma nova conta")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Recurso criado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição mal formatada"),
            @ApiResponse(code = 409, message = "Recurso já existe"),
            @ApiResponse(code = 500, message = "Exceção gerada") })
    public ResponseEntity<GetAccountModel> createAccount(@RequestBody @Valid CreateAccountModel createAccountModel) {
        var accountCreated = this.accountApplicationService.createAccount(createAccountModel.getBank(),
                createAccountModel.getBranch(),
                createAccountModel.getDocumentNumber(), createAccountModel.getDocumentType());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(accountCreated.getAccountNumber()).toUri();
        return ResponseEntity.created(uri).body(accountCreated);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping(value = "/{documentNumber}/{documentType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Obtem uma conta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recurso encontrado"),
            @ApiResponse(code = 400, message = "Requisição mal formatada"),
            @ApiResponse(code = 404, message = "Recurso não encontrado"),
            @ApiResponse(code = 500, message = "Exceção gerada") })
    public ResponseEntity<GetAccountModel> getAccount(@PathVariable @NotBlank @NotEmpty String documentNumber, @PathVariable EDocument documentType) {
        var account = this.accountApplicationService.getAccount(documentNumber, documentType);
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping(value = "/credit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Creditar em uma conta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operação concluida com sucesso"),
            @ApiResponse(code = 400, message = "Requisição mal formatada"),
            @ApiResponse(code = 404, message = "Recurso não encontrado"),
            @ApiResponse(code = 500, message = "Exceção gerada") })
    public ResponseEntity<CreditAccountModel> creditAccount(@RequestBody @Valid CreditAccountModel creditAccountModel) {
            this.accountApplicationService.creditAccount(creditAccountModel.getDocumentType(),
                            creditAccountModel.getDocumentNumber(), creditAccountModel.getAmount());
            return ResponseEntity.ok(creditAccountModel);
    }
    
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping(value = "/debit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Debitar uma conta")
    @ApiResponses(value = {
                    @ApiResponse(code = 200, message = "Operação concluida com sucesso"),
                    @ApiResponse(code = 400, message = "Requisição mal formatada"),
                    @ApiResponse(code = 404, message = "Recurso não encontrado"),
                    @ApiResponse(code = 500, message = "Exceção gerada") })
    public ResponseEntity<DebitAccountModel> debitAccount(@RequestBody @Valid DebitAccountModel debitAccountModel) {
            this.accountApplicationService.debitAccount(debitAccountModel.getDocumentType(),
                            debitAccountModel.getDocumentNumber(), debitAccountModel.getAmount());
            return ResponseEntity.ok(debitAccountModel);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Transferência entre contas")
    @ApiResponses(value = {
                    @ApiResponse(code = 200, message = "Operação concluida com sucesso"),
                    @ApiResponse(code = 400, message = "Requisição mal formatada"),
                    @ApiResponse(code = 404, message = "Recurso não encontrado"),
                    @ApiResponse(code = 500, message = "Exceção gerada") })
    public ResponseEntity<TransferAccountModel> transferAccount(@RequestBody @Valid TransferAccountModel transferAccountModel) {
            this.accountApplicationService.transferAccounts(transferAccountModel.getDocumentTypeSource(), transferAccountModel.getDocumentNumberSource(), 
                            transferAccountModel.getDocumentTypeDestiny(),
                            transferAccountModel.getDocumentNumberDestiny(), 
                            transferAccountModel.getAmount());
            return ResponseEntity.ok(transferAccountModel);
    }
}
