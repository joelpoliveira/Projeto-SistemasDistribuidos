package server;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.io.*;
import common.*;

public class StartServer {
    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println("Too many arguments. Usage: Main.java <port>");
            System.exit(1);
        }

        // Read config file
        HashMap<String, String> confs = new ConfigurationsParser("server/config.yaml").parse();
        
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress hostname = InetAddress.getByName(confs.get("mainHostname"));

            socket.setSoTimeout(1000);

            // Send message
            byte[] messageBytes = "MAIN WAKED?".getBytes();
            DatagramPacket messagePacket = new DatagramPacket(messageBytes, messageBytes.length,
                    hostname,
                    Integer.parseInt(confs.get("checkPort")));
            for (int i = 0; i < 3; i++) {
                socket.send(messagePacket);

                try {
                    socket.receive(messagePacket);
                    String message = new String(messagePacket.getData(),
                            messagePacket.getOffset(),
                            messagePacket.getLength(),
                            StandardCharsets.UTF_8);
                    // System.out.println(message);
                    if (message.equals("1")) {
                        new Server(confs, null, false, "2");
                        break;
                    } else if(message.equals("2")){
                        new Server(confs, null, false, "1");
                        break;
                    }else
                        throw new SocketTimeoutException();
                } catch (SocketTimeoutException e) {
                    if (i == 2) {
                        PrimaryVerification temp = new PrimaryVerification(confs, "1");
                        System.out.println("I am primary!");
                        new Server(confs, temp, true, "1");
                        break;
                    }
                }
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Error checking main:" + e.getMessage());
        }
    }

}
