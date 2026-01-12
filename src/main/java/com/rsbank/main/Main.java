package com.rsbank.main;

import com.rsbank.components.Client;
import java.util.ArrayList;
import java.util.List;

// 1.1.2 Creation of The main class for tests
public class Main {

    public static void main(String[] args) {
        // Load the client list (generating 3 clients as requested)
        List<Client> clients = generateClients(3);

        // Display the contents of the collection
        displayClients(clients);
    }

    // Method to generate a list of clients
    public static List<Client> generateClients(int numberOfClients) {
        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= numberOfClients; i++) {
            clients.add(new Client("name" + i, "firstname" + i));
        }
        return clients;
    }

    // Method to display clients using a Stream
    public static void displayClients(List<Client> clients) {
        System.out.println("--- Clients ---");
        // Using Java 8 Streams as required
        clients.forEach(System.out::println);
    }
}