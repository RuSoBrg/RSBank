package com.rsbank.main;

import com.google.gson.*;
import com.rsbank.components.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class Main {

    static void main() {
        // --- PART 1 SETUP ---
        List<Client> clients = generateClients(3);
        List<Account> accountList = generateAccounts(clients);
        Hashtable<Integer, Account> accountTable = createAccountTable(accountList);
        List<Flow> flows = generateFlows(accountList);

        // --- PART 2.1: LOAD FLOWS FROM JSON ---
        // We add the JSON flows to our existing list of flows
        List<Flow> jsonFlows = loadFlowsFromJson("data/test_flows.json");
        if (jsonFlows != null) {
            flows.addAll(jsonFlows);
            System.out.println("Loaded " + jsonFlows.size() + " flows from JSON.");
        }

        // --- PART 2.2: LOAD ACCOUNTS FROM XML ---
        // Load extra accounts and add them to Hashtable
        List<Account> xmlAccounts = loadAccountsFromXml("data/test_accounts.xml");
        if (xmlAccounts != null) {
            for (Account acc : xmlAccounts) {
                accountTable.put(acc.getAccountNumber(), acc);
            }
            System.out.println("Loaded " + xmlAccounts.size() + " accounts from XML.");
        }

        // --- PROCESS & DISPLAY ---
        updateAccounts(flows, accountTable);
        System.out.println("\n--- Final Account Status (Sorted) ---");
        displayAccountsSorted(accountTable);
    }

    // ---------------------------------------------------------
    // 2.1 JSON Method (Advanced)
    // ---------------------------------------------------------
    public static List<Flow> loadFlowsFromJson(String filePath) {
        List<Flow> flows = new ArrayList<>();
        try {
            Path path = Paths.get(filePath);
            String jsonContent = new String(Files.readAllBytes(path));

            // Custom Gson setup to handle LocalDate
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                            LocalDate.parse(json.getAsString()))
                    .create();

            // Interpret the JSON objects as 'Credit' flows for this exercise
            Credit[] creditArray = gson.fromJson(jsonContent, Credit[].class);
            flows.addAll(Arrays.asList(creditArray));

        } catch (Exception e) {
            System.out.println("Error reading JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return flows;
    }

    // ---------------------------------------------------------
    // 2.2 XML Method (Advanced)
    // ---------------------------------------------------------
    public static List<Account> loadAccountsFromXml(String filePath) {
        List<Account> accounts = new ArrayList<>();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("account");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String label = element.getElementsByTagName("label").item(0).getTextContent();
                    String clientName = element.getElementsByTagName("clientName").item(0).getTextContent();
                    // For XML loading, we create a dummy client just to satisfy the constructor
                    Client dummyClient = new Client(clientName, "");

                    // Assume XML accounts are Savings for this exercise
                    Account acc = new SavingsAccount(label, dummyClient);

                    // Manually set balance if provided (though standard logic usually starts at 0)
                    String balanceStr = element.getElementsByTagName("balance").item(0).getTextContent();
                    acc.setBalance(Double.parseDouble(balanceStr));

                    accounts.add(acc);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading XML: " + e.getMessage());
        }
        return accounts;
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