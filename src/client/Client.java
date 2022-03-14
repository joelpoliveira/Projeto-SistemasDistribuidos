package client;

import common.*;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    public static void main(String args[]) {
        int socketPort = 0;
        String username;
        boolean loggedIn = false;

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

        // ask username
        Scanner sc = new Scanner(System.in);
        System.out.print("username: ");
        username = sc.nextLine();
        //sc.close();


        // Create socket
        try (Socket s = new Socket(args[0], socketPort)) {
            System.out.println("Created socket = " + s);

            // Create input and output DataStreams
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String text = "";
            String[] temp;
            // sc = null;

            try  {
                // Scanner sc = new Scanner(System.in);
                // sc = new Scanner(System.in);

                while (true) {
                    System.out.print(in.readUTF() + "> ");
                    text = sc.nextLine();

                    if (!text.equals(""))
                        out.writeUTF(text);

                    temp = text.split(" ");

                    switch (temp[0]) {
                        case "login":
                            // username
                            //System.out.print(in.readUTF());
                            //out.writeUTF(sc.nextLine());
                            out.writeUTF(username);
                            // password
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());

                            // Server message
                            text = in.readUTF();
                            if (text.equals("Logged in"))
                                loggedIn = true;

                            System.out.println(text);
                            break;

                        case "logout":                                
                            System.out.println(in.readUTF());
                            break;

                        case "passwd":
                            // Password 1
                            // check if server asks for password. If not, user is not logged in
                            if (!(text = in.readUTF()).equals("New password: ")) {
                                System.out.println(text);
                                break;
                            }
                            System.out.print(text);
                            out.writeUTF(sc.nextLine());
                            // Password 2
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());

                            // Server message
                            System.out.println(in.readUTF());
                            break;

                        case "cd":
                            // Server message
                            if (!((text = in.readUTF()).equals("")))
                                System.out.println(text);
                            break;

                        case "ls":
                            // Server message
                            System.out.println(in.readUTF());
                            break;

                        case "send":
                            // Receive connection port
                            int downloadPort = Integer.parseInt(in.readUTF());
                            //System.out.println(downloadPort);
                            if (downloadPort == -1) {
                                System.out.println("Login is required");
                                break;
                            }
                            // Create thread to send file
                            new SendFile(args[0], downloadPort);
                            break;
                        
                        case "download":
                            break;

                        case "help":
                            System.out.println(in.readUTF());
                            break;

                        default:
                            break;
                    }
                }

            } finally {
                System.out.println("TESTE");
                sc.close();
            }

            // } catch (EOFException e) {
            //     System.out.println("sdasndasndi"); // Server closed socket
            // } catch (IOException e) {
            //     System.out.println("IO:" + e);
            // }

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("Server closed connection"); // Server closed socket
            // TODO connect to secondary server
        } catch (IOException e) {
            System.out.printf("Connection refused to %s:%s\n", args[0], args[1]); // Connection refused. Port not open on hostname
        }
    }
    
}
