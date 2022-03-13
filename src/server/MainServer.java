package server;

import java.net.*;
import java.io.*;

public class MainServer {
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

        try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
            System.out.println("Main server started");
            System.out.println("Created listen socket: " + listenSocket);
            while (true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("Created client socket: " + clientSocket);
                numero++;
                new Connection(clientSocket, numero, "MainServer");
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }
    }
}
