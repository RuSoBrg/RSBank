package com.rsbank.main;

import com.rsbank.components.Account;
import com.rsbank.components.Client;
import com.rsbank.components.CurrentAccount;
import com.rsbank.components.SavingsAccount;

import java.util.ArrayList;
import java.util.List;

// 1.1.2 Creation of the main class for tests
public class Main {

    static void main() {
        // 1.1.2 Load the client list
        List<Client> clients = generateClients(3);
        // 1.1.2 Display clients
        displayClients(clients);

        // 1.2.3 Load the account list (using the clients list)
        List<Account> accountList = generateAccounts(clients);
        // 1.2.3 Display accounts
        displayAccounts(accountList);
    }

    // 1.1.2 Generate Clients
    public static List<Client> generateClients(int numberOfClients) {
        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= numberOfClients; i++) {
            clients.add(new Client("name" + i, "firstname" + i));
        }
        return clients;
    }

    // 1.1.2 Display Clients
    public static void displayClients(List<Client> clients) {
        System.out.println("\n--- Clients ---");
        clients.forEach(System.out::println);
    }

    // 1.2.3 Generate Accounts
    public static List<Account> generateAccounts(List<Client> clients) {
        List<Account> accounts = new ArrayList<>();
        for (Client client : clients) {
            // Create a Savings and Current account for each client
            accounts.add(new SavingsAccount("Savings", client));
            accounts.add(new CurrentAccount("Current", client));
        }
        return accounts;
    }

    // 1.2.3 Display Accounts
    public static void displayAccounts(List<Account> accounts) {
        System.out.println("\n--- Accounts ---");
        accounts.forEach(System.out::println);
    }
}