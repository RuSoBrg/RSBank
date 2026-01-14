package com.rsbank.main;

import com.rsbank.components.*;
import java.time.LocalDate;
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
        displayAccountsSorted(accountTable);

        // 1.3.4 Load Flows
        List<Flow> flows = generateFlows(accountList);
        System.out.println("\n--- Flows created: " + flows.size() + " ---");
    }

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

    public static Hashtable<Integer, Account> createAccountTable(List<Account> accounts) {
        Hashtable<Integer, Account> accountTable = new Hashtable<>();
        for (Account account : accounts) {
            accountTable.put(account.getAccountNumber(), account);
        }
        return accountTable;
    }

    public static void displayAccountsSorted(Hashtable<Integer, Account> accountTable) {
        System.out.println("\n--- Accounts Sorted by Balance ---");
        accountTable.values().stream()
                .sorted(Comparator.comparingDouble(Account::getBalance))
                .forEach(System.out::println);
    }

    // --- NEW METHOD 1.3.4 ---
    public static List<Flow> generateFlows(List<Account> accounts) {
        List<Flow> flows = new ArrayList<>();
        // 1.3.4 Date of flows = now + 2 days
        LocalDate flowDate = LocalDate.now().plusDays(2);

        // 1. A debit of 50€ from account n°1
        flows.add(new Debit("Debit of 50", 1, 50.0, 1, true, flowDate));

        // 2. A credit of 100.50€ on all current accounts
        // Iterate through all accounts to find CurrentAccounts
        for (Account account : accounts) {
            if (account instanceof CurrentAccount) {
                flows.add(new Credit("Credit Current", 2, 100.50, account.getAccountNumber(), true, flowDate));
            }
        }

        // 3. A credit of 1500€ on all savings accounts
        for (Account account : accounts) {
            if (account instanceof SavingsAccount) {
                flows.add(new Credit("Credit Savings", 3, 1500.0, account.getAccountNumber(), true, flowDate));
            }
        }

        // 4. A transfer of 50 € from account n°1 to account n°2
        flows.add(new Transfert("Transfer", 4, 50.0, 2, true, flowDate, 1));

        return flows;
    }
}