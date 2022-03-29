package server;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Server implements Runnable {
    // String hostname;
    int port;
    int clientNumber;
    boolean isPrimary;
    PrimaryVerification responder;
    String serverName;
    Thread t;
    HashMap<String, String> configs;
    

    public Server( HashMap<String, String> configs, PrimaryVerification responder, boolean isPrimary) {
        // this.hostname = hostname;
        this.configs=configs;
        this.isPrimary = isPrimary;
        this.responder = responder;
        //this.serverName = name;
        this.clientNumber = 0;
        this.update();
        this.t = new Thread(this);
        this.t.start();
        
    }

    public void run(){

        
            System.out.println(this.serverName + " server started");
            //System.out.println(this.serverName + " listen socket: " + listenSocket);
            ReceiveFileUDP udp_receiver = null;
            HearthBeatReceiver receiver = null;
            HearthBeatSender sender = null;

            while (true) {
                System.out.println("Primaty = " + this.isPrimary);
                if (this.isPrimary){
                    try (ServerSocket listenSocket = new ServerSocket(this.port)) {
                        if (receiver == null){
                            receiver =  new HearthBeatReceiver( Integer.parseInt( configs.get("heartBeatPort") ) );
                        }
                        if(responder == null){
                            responder = new PrimaryVerification(configs);
                        }
                        Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                        System.out.println("Client connect to " + this.serverName);
                        this.clientNumber++;
                        new Connection(clientSocket, this.clientNumber, this.serverName);

                    } catch (IOException e) {
                        System.out.println("Listen:" + e.getMessage());
                    }
                }else{
                    
                    if (receiver != null && !receiver.t.isInterrupted()){
                        receiver.interrupt();
                        receiver = null;
                    }
                    if (responder !=null && !responder.t.isInterrupted()){
                        responder.interrupt();
                        responder=null;
                    }
                    sender = new HearthBeatSender(this, configs);
                    udp_receiver = new ReceiveFileUDP(Integer.parseInt(configs.get("serverUdpPort")));
                    
                    synchronized (this) {
                        try{
                            wait(); 
                        } catch (InterruptedException e){
                            System.out.println("Interrupted"); 
                        }
                        this.isPrimary = true;
                        sender = null;
                        
                        udp_receiver.interrupt();
                        udp_receiver = null;
                        this.update();
                    }                        
                }
            
            }
        

    }

    public void update(){
        if (this.isPrimary)
            this.port = Integer.parseInt(configs.get("mainPort"));
        else 
            this.port = Integer.parseInt(configs.get("secondaryPort"));
    }
    
}
