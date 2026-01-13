package com.rsbank.main;

import com.rsbank.components.Account;
import com.rsbank.components.Client;
import com.rsbank.components.CurrentAccount;
import com.rsbank.components.SavingsAccount;

import java.util.*;

public class Main {

    static void main() {
        // 1.1.2 Load clients
        List<Client> clients = generateClients(3);
        displayClients(clients);

        // 1.2.3 Load accounts
        List<Account> accountList = generateAccounts(clients);
        displayAccounts(accountList);

        // 1.3.1 Adapt accounts to Hashtable
        Hashtable<Integer, Account> accountTable = createAccountTable(accountList);

        // 1.3.1 Display accounts sorted by balance
        displayAccountsSorted(accountTable);
    }

    // --- EXISTING METHODS ---
    public static List<Client> generateClients(int numberOfClients) {
        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= numberOfClients; i++) {
            clients.add(new Client("name" + i, "firstname" + i));
        }
        return clients;
    }

    public static void displayClients(List<Client> clients) {
        System.out.println("\n--- Clients ---");
        clients.forEach(System.out::println);
    }

    public static List<Account> generateAccounts(List<Client> clients) {
        List<Account> accounts = new ArrayList<>();
        for (Client client : clients) {
            accounts.add(new SavingsAccount("Savings", client));
            accounts.add(new CurrentAccount("Current", client));
        }
        return accounts;
    }

    public static void displayAccounts(List<Account> accounts) {
        System.out.println("\n--- Accounts (List) ---");
        accounts.forEach(System.out::println);
    }

    // --- NEW METHODS FOR 1.3.1 ---

    // Convert List to Hashtable
    public static Hashtable<Integer, Account> createAccountTable(List<Account> accounts) {
        Hashtable<Integer, Account> accountTable = new Hashtable<>();
        for (Account account : accounts) {
            // Key = Account Number, Value = Account Object
            accountTable.put(account.getAccountNumber(), account);
        }
        return accountTable;
    }

    // Display Map sorted by Balance (Ascending)
    public static void displayAccountsSorted(Hashtable<Integer, Account> accountTable) {
        System.out.println("\n--- Accounts Sorted by Balance ---");
        accountTable.values().stream()
                .sorted(Comparator.comparingDouble(Account::getBalance))
                .forEach(System.out::println);
    }
}