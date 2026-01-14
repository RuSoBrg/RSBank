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

        // 1.3.5 Update Accounts
        updateAccounts(flows, accountTable);

        // Display results after updates
        System.out.println("\n--- Accounts after updates ---");
        displayAccountsSorted(accountTable);
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

    public static List<Flow> generateFlows(List<Account> accounts) {
        List<Flow> flows = new ArrayList<>();
        LocalDate flowDate = LocalDate.now().plusDays(2);

        flows.add(new Debit("Debit of 50", 1, 50.0, 1, true, flowDate));

        for (Account account : accounts) {
            if (account instanceof CurrentAccount) {
                flows.add(new Credit("Credit Current", 2, 100.50, account.getAccountNumber(), true, flowDate));
            }
        }

        for (Account account : accounts) {
            if (account instanceof SavingsAccount) {
                flows.add(new Credit("Credit Savings", 3, 1500.0, account.getAccountNumber(), true, flowDate));
            }
        }

        flows.add(new Transfert("Transfer", 4, 50.0, 2, true, flowDate, 1));

        return flows;
    }

    // --- NEW METHOD 1.3.5 ---
    public static void updateAccounts(List<Flow> flows, Hashtable<Integer, Account> accountTable) {
        for (Flow flow : flows) {
            // 1. Update the Target Account
            Account target = accountTable.get(flow.getTargetAccountNumber());
            if (target != null) {
                target.setBalance(flow);
            }

            // 2. If it is a Transfert, we must also update the Issuer Account
            if (flow instanceof Transfert t) {
                Account issuer = accountTable.get(t.getIssuingAccountNumber());
                if (issuer != null) {
                    issuer.setBalance(flow);
                }
            }
        }

        // Check for negative balance using Predicate and Optional
        Optional<Account> negativeAccount = accountTable.values().stream()
                .filter(a -> a.getBalance() < 0)
                .findAny();

        negativeAccount.ifPresent(account -> System.out.println("ALERT: Account with negative balance found: Account n°"
                + account.getAccountNumber()));
    }
}