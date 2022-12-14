package client;

import common.*;

import java.net.*;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

public class Client {
    static String mainHostname;
    static int mainPort;
    static String secondaryHostname;
    static int secondaryPort;

    public static void main(String args[]) {
        String username = "";
        HashMap<String, String> config = new ConfigurationsParser("client/config.yaml").parse();

        mainHostname = config.get("mainHostname");
        mainPort = Integer.parseInt(config.get("mainPort"));
        secondaryHostname = config.get("secondaryHostname");
        secondaryPort = Integer.parseInt(config.get("secondaryPort"));

        Scanner sc = new Scanner(System.in);

        // Ask for a username. Keep asking for a valid one
        username = askUsername(sc);

        connect(mainHostname, mainPort, username, false);
    }

    public static void connect(String hostname, int port, String username, boolean isSecondary) {
        boolean loggedIn = false;
        // String username = "";
        Scanner sc = new Scanner(System.in);

        try (Socket s = new Socket(hostname, port)) {
            System.out.println("Created socket: " + s);

            // Create input and output DataStreams
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String text = "";
            String[] temp;
            String serverPath = "";
            String clientPath = "home/";
            // sc = null;

            // try {
            while (true) {

                if (!loggedIn)
                    System.out.print(username + "/" + clientPath + "> ");
                else
                    System.out.print(serverPath + "> ");

                text = sc.nextLine();

                temp = text.split(" ");

                switch (temp[0]) {
                    case "login":

                        if (!loggedIn) {
                            out.writeUTF(text);

                            // username
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
                        } else {
                            System.out.println("Already logged in");
                        }
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
                            System.out.print(in.readUTF());
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
                            if (text.equals("Directory doesn't exist") || text.equals("Not a directory"))
                                System.out.println(text);
                            else
                                serverPath = text;
                        } else {
                            // Local cd
                            try {
                                clientPath = changeDirectory(clientPath, temp[1], username);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                clientPath = changeDirectory(clientPath, "", username);
                            }

                        }
                        break;

                    case "ls":
                        if (loggedIn) {
                            out.writeUTF(text);
                            // Server message
                            text = in.readUTF();
                            System.out.println(text);
                        } else {
                            System.out.println(listDirectory(clientPath, username));
                        }
                        break;

                    case "send":
                        if (loggedIn) {
                            // check params
                            // send file_path_client file_path_server
                            String source = "", destination = "";

                            try {
                                source = temp[1];
                                destination = temp[2];
                            } catch (ArrayIndexOutOfBoundsException e) {
                                System.out.println("Command usage: send <source> <destination>");
                                break;
                            }

                            out.writeUTF(text);

                            // Receive connection port
                            int downloadPort = Integer.parseInt(in.readUTF());
                            // System.out.println(downloadPort);

                            // Send file destination
                            out.writeUTF(destination);

                            // Create thread to send file
                            new SendFile(username, hostname, downloadPort, source);
                        } else {
                            System.out.println("Login is required");
                        }

                        break;

                    case "download":
                        // String source = "";
                        String destination = "";
                        if (loggedIn) {

                            try {
                                // source = temp[1];
                                destination = temp[2];
                            } catch (ArrayIndexOutOfBoundsException e) {
                                System.out.println("Command usage: download <souce> <destination>");
                                break;
                            }

                            out.writeUTF(text);
                            // Receive connection port
                            int downloadPort = Integer.parseInt(in.readUTF());
                            // System.out.println(downloadPort);

                            // Create thread to send file
                            new ReceiveFile(username, hostname, downloadPort, destination);

                        } else {
                            System.out.println("Login is required");
                        }
                        break;

                    case "config":
                        try {
                            switch (temp[1]) {
                                case "mainHostname":
                                    mainHostname = temp[2];
                                    break;
                                case "secondaryHostname":
                                    secondaryHostname = temp[2];
                                    break;
                                case "mainPort":
                                    mainPort = Integer.parseInt(temp[2]);
                                    break;
                                case "secondaryPort":
                                    secondaryPort = Integer.parseInt(temp[2]);
                                    break;
                                default:
                                    System.out.println(
                                            "Can only change mainHostname, secondaryHostname, mainPort, secondaryPort");
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println(
                                    "Command usage: config <option> <value>\n\toptions:\n\t\tmainHostname\n\t\tsecondaryHostname\n\t\tmainPort\n\t\tsecondaryPort");
                        } catch(NumberFormatException nfe){
                            System.out.println("Port value must be an integer.");
                        }
                        break;

                    case "help":
                        if (loggedIn){
                            out.writeUTF(text);
                            System.out.println(in.readUTF());
                        }else{
                            System.out.println("""
                                Available commands:
                                cd <path>               - changes directory to <path>
                                download <source> <dest>- *copy server file <source> to local <dest>
                                login                   - login to server
                                logout                  - *disconects from server
                                ls                      - lists current directory files and folders
                                passwd                  - *change password
                                send <source> <dest>    - *copy local file <source> to server <dest>
                                config <option> <value> - change configurations
                                                        - options: mainHostname
                                                                   secondaryHostname
                                                                   mainPort
                                                                   secondaryPort
                                '*' indicates that for that command, login is required.
                                """);
                        }
                        break;

                    case "exit":
                        loggedIn = false;
                        System.exit(0);
                        username = askUsername(sc);
                        break;

                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            // Server closed socket
            System.out.println("EOF: " + e.getMessage());
            if (!isSecondary) {
                System.out.println("Connecting to Secondary server...");
                connect("localhost", 8001, username, true);
            }
        } catch (IOException e) {
            // Connection refused. Broken pipe
            System.out.println("IO: " + e.getMessage());
            if (!isSecondary) {
                System.out.println("Connecting to Secondary server...");
                connect("localhost", 8001, username, true);
            }
        }

    }

    public static String askUsername(Scanner sc) {
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

            if (!exist) {
                System.out.println("User doesn't exist");
            }
        }
        return username;
    }

    public static String listDirectory(String clientPath, String username) {
        try {
            StringBuilder result = new StringBuilder();

            File folder = new File("client/users/" + username + "/" + clientPath);
            File[] files = folder.listFiles();

            for (File file : files) {
                if (file.isDirectory())
                    result.append("*" + file.getName() + "*");
                else
                    result.append(file.getName());

                result.append("\t");
            }

            return result.toString();

        } catch (Exception e) {
            System.out.println("Erro listing directory files");
            return "";
        }
    }

    public static String changeDirectory(String clientPath, String newDirectory, String username) {
        String newDir = "";
        File f = new File("client/users/" + username + "/" + clientPath + "/" + newDirectory);

        if (newDirectory.equals("")) {
            newDir = "home/";
        } else {
            if (f.exists() && f.isDirectory()) {
                newDir = "home/" + newDirectory;
            } else if (f.exists() && !f.isDirectory()) {
                System.out.println("Not a directory");
                newDir = clientPath;
            } else {
                System.out.println("Doesn't exist");
                newDir = clientPath;
            }
        }

        return newDir;
    }

}
