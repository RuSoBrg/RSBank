package com.rsbank.components;

import java.time.LocalDate;

public class Credit extends Flow {
    public Credit(String comment, int identifier, double amount, int targetAccountNumber, boolean effect, LocalDate dateOfFlow) {
        super(comment, identifier, amount, targetAccountNumber, effect, dateOfFlow);
    }
}