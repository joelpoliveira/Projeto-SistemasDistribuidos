package projeto;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class MainServer {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        int numero = 0;
        int serverPort = 0;

        if (args.length > 1) {
            System.out.println("Too many arguments. MainServer.java <port>");
            System.exit(1);
        }

        if (args.length < 1) {
            System.out.println("Missing arguments. MainServer.java <port>");
            System.exit(1);
        }

        try {
            serverPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Try to open credentials file
        // If execptions is thown -> file didnt exist -> create it
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream("credentials"));
            i.close();
        } catch (IOException e) {
            System.out.println("Creating credentials file");
            try {
                HashMap<String, String> credentials = new HashMap<String, String>();
                ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("credentials"));
                o.writeObject(credentials);
                o.close();
            } catch (IOException ex) {
                System.out.println("Failed to create credentials file");
            }
        }


        try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
            System.out.println("Main server started");
            System.out.println("Created listen socket: " + listenSocket);
            while (true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("Created client socket: " + clientSocket);
                numero++;
                new Connection(clientSocket, numero);
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }
    }
}
