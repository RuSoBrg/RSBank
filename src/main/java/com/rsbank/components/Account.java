package com.rsbank.components;

// 1.2.1 Creation of the account class
public abstract class Account {
    // Attributes (Protected as requested)
    protected String label;
    protected double balance;
    protected int accountNumber;
    protected Client client;

    // Static counter for unique account numbers
    private static int numberOfAccounts = 0;

    // Constructor
    public Account(String label, Client client) {
        this.label = label;
        this.client = client;
        this.balance = 0.0; // Default balance is 0
        this.accountNumber = ++numberOfAccounts;
    }

    // Accessors & Mutators
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public double getBalance() { return balance; }
    // 1.2.1: Setter currently takes an amount (double). Will be modified in 1.3.5.
    public void setBalance(double balance) { this.balance = balance; }

    public int getAccountNumber() { return accountNumber; }
    // No setter for account number (it's auto-generated)

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    // toString method
    @Override
    public String toString() {
        return "Account{" +
                "label='" + label + '\'' +
                ", balance=" + balance +
                ", accountNumber=" + accountNumber +
                ", client=" + client.getName() + // displaying client name for clarity
                '}';
    }
}