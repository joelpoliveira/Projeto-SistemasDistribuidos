package server;

import common.*;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Connection implements Runnable {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int threadNumber;
    Thread t;
    String serverName;
    User user;
    FileHandler fh;
    int downloadId;

    public Connection(Socket clientSocket, int number, String serverName) {
        this.threadNumber = number;
        this.serverName = serverName;
        this.downloadId = 0;
        try {
            this.clientSocket = clientSocket;
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            this.t = new Thread(this, Integer.toString(threadNumber));

            this.fh = new FileHandler();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }

        // Start Thread
        this.t.start();
    }

    // ================== Thread main
    public void run() {
        String text = "";
        String[] temp;
        try {

            while (true) {
                // System.out.println("text = " + text);
                if (this.user != null) {
                    // this.out.writeUTF(this.serverName + "@" + this.user.username + "/" +
                    // this.user.currentDirectory);
                    System.out.printf("Received %s from %d -> %s\n", text, threadNumber, this.user.username);
                } else {
                    // this.out.writeUTF("");
                    System.out.printf("Received %s from %d\n", text, threadNumber);
                }

                text = this.in.readUTF();
                temp = text.split(" ");

                switch (temp[0]) {
                    case "login":
                        login();
                        break;

                    case "logout":
                        if (this.user != null) {
                            this.user = null;
                            this.out.writeUTF("Logged out");
                        } else 
                            this.out.writeUTF("Login is required");

                        break;

                    case "passwd":
                        changePassword();
                        break;

                    case "cd":
                        if (this.user != null) {
                            try {
                                System.out.println("Changing to " + temp[1]);
                                changeDirectory(temp[1]);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                // System.out.println("Out of Bounds");
                                changeDirectory("");
                            }
                        }
                        break;

                    case "ls":
                        listDirectory();
                        break;

                    // Client -> Server
                    case "send":
                        receiveFile();
                        break;

                    // Server -> Client
                    case "download":
                        sendFile(temp[1]);
                        break;

                    case "help":
                        this.out.writeUTF("""
                                Available commands:
                                login - login to server
                                passwd - change password (requires login)
                                cd - changes directory. cd [path]
                                """);
                        break;

                    default:
                        System.out.println("Unknown command");
                        // if (this.user != null)
                        // this.out.writeUTF("");
                        break;
                }
            }

        } catch (EOFException e) {
            // Client closed socket
            if (this.user != null)
                System.out.printf("Client %d -> %s closed connection\n", this.threadNumber, this.user.username);
            else
                System.out.printf("Client %d closed connection\n", this.threadNumber);
        } catch (IOException e) {
            if (this.user != null)
                // Error reading from pipe. Client closed connection
                System.out.printf("Client %d -> %s closed connection\n", this.threadNumber, this.user.username);
            else
                System.out.printf("Client %d closed connection\n", this.threadNumber);
        }

    }

    public void login() {
        String username = "", password = "";
        ArrayList<String> credentials;
        int index = 0;
        this.fh = new FileHandler();

        try {
            // this.out.writeUTF("username: ");
            username = this.in.readUTF();
            // System.out.println(username);

            this.out.writeUTF("password: ");
            password = this.in.readUTF();
            // System.out.println(password);

            // Read credentials file and check user
            credentials = fh.readFile("server/users/.credentials");
            if ((index = credentials.indexOf(username)) == -1) {
                // System.out.println("User doesn't exist");
                this.out.writeUTF("User doesnt exist");
                return;
            } else {
                if (credentials.get(index + 1).equals(password)) {
                    // System.out.println("Logged in");
                    this.out.writeUTF("Logged in");
                } else {
                    // System.out.println("Wrong password");
                    this.out.writeUTF("Wrong password");
                    return;
                }
            }

            // Associate a user with this connection
            this.user = new User("server/users/" + username + "/.config");
            this.user.loggedIn = true;

            // Send directory to client
            this.out.writeUTF(this.serverName + "@" + this.user.getFullPath());

        } catch (IOException e) {
            System.out.println("Error login");
        }
    }

    public void changePassword() {
        String password1 = "", password2 = "";
        ArrayList<String> temp = fh.readFile("server/users/.credentials");
        int index;

        try {
            if (this.user != null) {
                this.out.writeUTF("New password: ");
                password1 = this.in.readUTF();

                this.out.writeUTF("Repeat password: ");
                password2 = this.in.readUTF();

                if (password1.equals(password2)) {
                    // change password in .credentials file
                    index = temp.indexOf(this.user.username);
                    temp.set(index + 1, password1);
                    this.fh.reWriteFile("server/users/.credentials", temp);
                    // change user config file password
                    temp = this.fh.readFile("server/users/" + this.user.username + "/.config");
                    temp.set(1, password1);
                    this.fh.reWriteFile("server/users/" + this.user.username + "/.config", temp);

                    this.user.password = password1;
                    this.out.writeUTF("Password changed!");

                } else {
                    this.out.writeUTF("Passwords don't match");
                    return;
                }

            } else {
                this.out.writeUTF("User not looged in");
            }

        } catch (IOException e) {
            System.out.println("Erro sendig message");
        }

    }

    public void changeDirectory(String newDirectory) {
        ArrayList<String> config;

        try {
            if (new File("server/users/" + this.user.username + "/home/" + newDirectory).exists()) {
                this.user.currentDirectory = "home/" + newDirectory;
                config = this.fh.readFile("server/users/" + this.user.username + "/.config");
                config.set(2, "home/" + newDirectory);
                this.fh.reWriteFile("server/users/" + this.user.username + "/.config", config);

                // send server info + directory
                // System.out.println("==== " + this.serverName + "@" +
                // this.user.getFullPath());
                this.out.writeUTF(this.serverName + "@" + this.user.getFullPath());
            } else {
                this.out.writeUTF("Directory doesn't exist. Please use full path");
            }

        } catch (IOException e) {
            System.out.println("Erro changing directory");
        }
    }

    public void listDirectory() {
        try {
            StringBuilder result = new StringBuilder();

            // System.out.println("Full path = " + this.user.getFullPath());

            File folder = new File("server/users/" + this.user.getFullPath());
            File[] files = folder.listFiles();

            for (File file : files) {
                if (file.isDirectory()) {
                    // System.out.println("*" + file.getName() + "*");
                    result.append("*" + file.getName() + "*");
                } else {
                    // System.out.println(file.getName());
                    result.append(file.getName());
                }
                result.append("\t");
            }

            this.out.writeUTF(result.toString());

        } catch (IOException e) {
            System.out.println("Erro listing directory files");
        }
    }

    // Receive file from client
    public void receiveFile() {
        String destination = "";

        // if port set to 0, a random port wil be used
        try (ServerSocket downloadSocket = new ServerSocket(0)) {
            System.out.println("Download socket created: " + downloadSocket);
            
            // Send port to client
            this.out.writeUTF(Integer.toString(downloadSocket.getLocalPort()));

            // Read file destination
            destination = this.in.readUTF();

            Socket soc = downloadSocket.accept(); // BLOQUEANTE

            System.out.printf("Client %d connected to download socket\n", this.threadNumber);
            //this.downloadId++;
            new ReceiveFile(soc, destination, this.user.username);

        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }

    }

    // Send file to client
    // download Desktop/file file123
    public void sendFile(String source) {
        // if port set to 0, a random port wil be used
        try (ServerSocket downloadSocket = new ServerSocket(0)) {
            System.out.println("Download socket created: " + downloadSocket);
            
            // Send port to client
            this.out.writeUTF(Integer.toString(downloadSocket.getLocalPort()));

            Socket soc = downloadSocket.accept(); // BLOQUEANTE

            System.out.printf("Client %d connected to download socket\n", this.threadNumber);
            // this.downloadId++;
            new SendFile(soc, source, this.user.username);

        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }
    }

}
