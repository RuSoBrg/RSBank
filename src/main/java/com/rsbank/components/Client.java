package com.rsbank.components;

// 1.1.1 Creation of the client class
public class Client {
    // Attributes
    private String name;
    private String firstName;
    private int clientNumber;

    // Static counter to auto-increment client numbers
    private static int numberOfClients = 0;

    // Constructor
    public Client(String name, String firstName) {
        this.name = name;
        this.firstName = firstName;
        // Auto-increment logic
        this.clientNumber = ++numberOfClients;
    }

    // Accessors (Getters) & Mutators (Setters)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public int getClientNumber() { return clientNumber; }
    public void setClientNumber(int clientNumber) { this.clientNumber = clientNumber; }

    // toString method
    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", clientNumber=" + clientNumber +
                '}';
    }
}