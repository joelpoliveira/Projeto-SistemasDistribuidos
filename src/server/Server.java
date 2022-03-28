package server;

import java.net.*;
import java.io.*;

public class Server implements Runnable {
    // String hostname;
    int port;
    int clientNumber;
    boolean isPrimary;
    Boolean lock;
    String serverName;
    Thread t;
    

    public Server(int port, String name, boolean isPrimary) {
        // this.hostname = hostname;
        this.port = port;
        this.serverName = name;
        this.clientNumber = 0;
        this.isPrimary = isPrimary;
        this.t = new Thread(this, name);
        this.t.start();
    }

    public void run(){

        try (ServerSocket listenSocket = new ServerSocket(this.port)) {
            System.out.println(this.serverName + " server started");
            System.out.println(this.serverName + " listen socket: " + listenSocket);
            HearthBeatReceiver receiver = null;
            HearthBeatSender sender = null;
            while (true) {

                if (this.isPrimary){
                    if (receiver == null){
                        receiver =  new HearthBeatReceiver();
                    }
                    Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                    System.out.println("Client connect to " + this.serverName);
                    this.clientNumber++;
                    new Connection(clientSocket, this.clientNumber, this.serverName);
                
                }else{
                    if (receiver != null && receiver.t.isAlive()){
                        receiver.interrupt();
                        receiver = null;
                    }
                    sender = new HearthBeatSender(this);
                    new ReceiveFileUDP();
                    
                    synchronized (this) {
                        try{
                            wait();
                        } catch (InterruptedException e){
                            System.out.println("Interrupted");
                        }
                    }    

                    this.isPrimary = true;
                    sender = null;
                    System.out.println("I am primary");
                    
                }
            
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }

    }
    
}
