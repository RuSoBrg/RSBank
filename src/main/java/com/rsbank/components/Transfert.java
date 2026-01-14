package com.rsbank.components;

import java.time.LocalDate;

public class Transfert extends Flow {
    private int issuingAccountNumber;

    public Transfert(String comment, int identifier, double amount, int targetAccountNumber, boolean effect, LocalDate dateOfFlow, int issuingAccountNumber) {
        super(comment, identifier, amount, targetAccountNumber, effect, dateOfFlow);
        this.issuingAccountNumber = issuingAccountNumber;
    }

    public int getIssuingAccountNumber() { return issuingAccountNumber; }
    public void setIssuingAccountNumber(int issuingAccountNumber) { this.issuingAccountNumber = issuingAccountNumber; }
}