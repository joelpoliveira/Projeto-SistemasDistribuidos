package server;

import java.net.*;
import java.io.*;

public class Server implements Runnable {
    // String hostname;
    int port;
    String serverName;
    Thread t;
    int clientNumber;

    public Server(int port, String name) {
        // this.hostname = hostname;
        this.port = port;
        this.serverName = name;
        this.clientNumber = 0;
        this.t = new Thread(this, name);
        // this.t.setDaemon(true);
        this.t.start();
    }

    public void run(){
        try (ServerSocket listenSocket = new ServerSocket(this.port)) {
            System.out.println(this.serverName + " server started");
            System.out.println(this.serverName + " listen socket: " + listenSocket);
            while (true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                // System.out.println("Client connect to" + this.serverName + ": " + clientSocket);
                System.out.println("Client connect to " + this.serverName);
                this.clientNumber++;
                new Connection(clientSocket, this.clientNumber, this.serverName);
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }

    }
    
}
