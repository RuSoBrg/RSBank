package com.rsbank.components;

import java.time.LocalDate;

public class Debit extends Flow {
    public Debit(String comment, int identifier, double amount, int targetAccountNumber, boolean effect, LocalDate dateOfFlow) {
        super(comment, identifier, amount, targetAccountNumber, effect, dateOfFlow);
    }
}