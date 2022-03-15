package client;

import common.*;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    public static void main(String args[]) {
        int socketPort = 0;
        String username = "";
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

        Scanner sc = new Scanner(System.in);

        // Ask for a username. Keep asking for a valid one
        username = askUsername(sc);
        

        // Create socket
        try (Socket s = new Socket(args[0], socketPort)) {
            System.out.println("Created socket: " + s);

            // Create input and output DataStreams
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String text = "";
            String[] temp;
            String serverPath = "";
            // sc = null;

            try {
                // Scanner sc = new Scanner(System.in);
                // sc = new Scanner(System.in);

                while (true) {
                    //System.out.println("text = " + text);

                    if (!loggedIn)
                        System.out.print("> ");
                    else {
                        // Only update serverPath on cd and login
                        // if(/* !text.equals("") && */text.equals("Logged in"))
                        // serverPath = in.readUTF();
                        System.out.print(serverPath + "> ");
                    }

                    text = sc.nextLine();

                    // if (!text.equals(""))
                    // out.writeUTF(text);

                    temp = text.split(" ");

                    switch (temp[0]) {
                        case "login":
                            out.writeUTF(text);

                            // username
                            // System.out.print(in.readUTF());
                            // out.writeUTF(sc.nextLine());
                            out.writeUTF(username);
                            // password
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());

                            // Server message
                            text = in.readUTF();
                            if (text.equals("Logged in")) {
                                loggedIn = true;
                                serverPath = in.readUTF();
                            }

                            System.out.println(text);
                            break;

                        case "logout":
                            out.writeUTF(text);

                            loggedIn = false;
                            System.out.println(in.readUTF());
                            break;

                        case "passwd":
                            if (loggedIn) {
                                out.writeUTF(text);

                                // Password 1
                                // check if server asks for password. If not, user is not logged in
                                // if (!(text = in.readUTF()).equals("New password: ")) {
                                // System.out.println(text);
                                // break;
                                // }

                                System.out.print(text);
                                out.writeUTF(sc.nextLine());
                                // Password 2
                                System.out.print(in.readUTF());
                                out.writeUTF(sc.nextLine());

                                // Server message
                                System.out.println(in.readUTF());

                            } else {
                                System.out.println("Login Required!");
                            }
                            break;

                        case "cd":
                            if (loggedIn) {
                                out.writeUTF(text);

                                // Server message
                                text = in.readUTF();
                                if (text.equals("Directory doesn't exist"))
                                    System.out.println(text);
                                else
                                    serverPath = text;
                            } else {
                                System.out.println("TODO Local cd");
                            }
                            break;

                        case "ls":
                            if (loggedIn) {
                                out.writeUTF(text);
                                // Server message
                                text = in.readUTF();
                                System.out.println(text);
                            } else {
                                System.out.println("TODO Local ls");
                            }
                            break;

                        case "send":
                            if (loggedIn) {
                                out.writeUTF(text);

                                // Receive connection port
                                int downloadPort = Integer.parseInt(in.readUTF());
                                // System.out.println(downloadPort);

                                // if (downloadPort == -1) {
                                // System.out.println("Login is required");
                                // break;
                                // }

                                // Create thread to send file
                                new SendFile(args[0], downloadPort);

                            } else {
                                System.out.println("Login is required");
                            }

                            break;

                        case "download":
                            if (loggedIn){
                                out.writeUTF(text);
                                System.out.println("TODO download");
                            } else {
                                System.out.println("Login is required");
                            }
                            break;

                        case "help":
                            out.writeUTF(text);
                            System.out.println(in.readUTF());
                            break;
                        
                        case "exit":
                            username = askUsername(sc);
                            break;

                        default:
                            System.out.println("Unknown command");
                            break;
                    }
                }

            } finally {
                System.out.println("TESTE");
                sc.close();
            }

            // } catch (EOFException e) {
            // System.out.println("sdasndasndi"); // Server closed socket
            // } catch (IOException e) {
            // System.out.println("IO:" + e);
            // }

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("Server closed connection"); // Server closed socket
            // TODO connect to secondary server
        } catch (IOException e) {
            System.out.printf("Connection refused to %s:%s\n", args[0], args[1]); // Connection refused. Port not open
                                                                                  // on hostname
        }
    }

    public static String askUsername(Scanner sc){
        String username = "";
        boolean exist = false;

        File[] users = new File("client/users/").listFiles();
        while (!exist) {
            System.out.print("username: ");
            username = sc.nextLine();

            for (File user : users) {
                if (user.getName().equals(username)) {
                    exist = true;
                    break;
                }
            }

            if (!exist){
                System.out.println("User doesn't exist");
            }
        }
        return username;
    }

}
