package projeto;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    public static void main(String args[]) {
        int socketPort = 0;

        // args[0] <- hostname of destination
        // args[1] <- port of destination
        if (args.length > 2) {
            System.out.println("Too many arguments. Client.java <hostname> <port>");
            System.exit(1);
        }

        if (args.length < 2) {
            System.out.println("Missing arguments. Client.java <hostname> <port>");
            System.exit(1);
        }

        try {
            socketPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            // e.printStackTrace();
            System.out.println("Port must be an integer");
            System.exit(1);
        }

        // Create socket
        try (Socket s = new Socket(args[0], socketPort)) {
            System.out.println("Created socket = " + s);

            // Create input and output DataStreams
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String text = "";

            try {
                Scanner sc = new Scanner(System.in);

                while (true) {
                    System.out.print("> ");
                    // System.out.print(in.readUTF());
                    text = sc.nextLine();
                    
                    if (!text.equals(""))
                        out.writeUTF(text);
                    
                    switch (text) {
                        case "register":
                            // username
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());
                            // password
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());

                            // Server message
                            System.out.println(in.readUTF());
                            break;

                        case "login":
                            // username
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());
                            // password
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());

                            // Server message
                            System.out.println(in.readUTF());
                            break;
                        
                        case "change":
                            // Password 1
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());
                            // Password 2
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());

                            // Server message
                            System.out.println(in.readUTF());
                            break;

                        case "teste":
                            break;

                        default:
                            break;
                    }
                }

            } catch (EOFException e) {
                System.out.println("EOF:" + e); // Server closed socket
            } catch (IOException e) {
                System.out.println("IO:" + e);
            }

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage()); // Server socket not open anymore
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage()); // Connection refused. Port not open on hostname
        }
    }
}
