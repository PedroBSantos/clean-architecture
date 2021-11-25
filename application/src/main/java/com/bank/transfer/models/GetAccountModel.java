package com.bank.transfer.models;

import java.util.UUID;

public class GetAccountModel {

    private String bank;
    private String branch;
    private UUID accountNumber;
    private float balance;

    public GetAccountModel() {
        super();
    }

    public GetAccountModel(String bank, String branch, UUID accountNumber, float balance) {
        this.bank = bank;
        this.branch = branch;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public UUID getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(UUID accountNumber) {
        this.accountNumber = accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
