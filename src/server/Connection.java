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

    public Connection(Socket clientSocket, int number, String serverName) {
        this.threadNumber = number;
        this.serverName = serverName;
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
            // FileHandler fh = new FileHandler("credentials");

            while (true) {
                if (this.user != null) {
                    this.out.writeUTF(this.serverName + " " + this.user.username + "/" + this.user.currentDirectory);
                    System.out.printf("Received %s from %d -> %s\n", text, threadNumber, this.user.username);
                } else {
                    this.out.writeUTF("");
                    System.out.printf("Received %s from %d\n", text, threadNumber);
                }

                text = this.in.readUTF();
                temp = text.split(" ");

                // try{
                // if (temp[1].equals("")){}

                // } catch (ArrayIndexOutOfBoundsException e){}

                switch (temp[0]) {
                    case "login":
                        login();
                        break;

                    case "passwd":
                        changePassword();
                        break;

                    case "cd":
                        try {
                            changeDirectory(temp[1]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            changeDirectory("");
                        }
                        break;

                    case "ls":
                        listDirectory();
                        break;

                    case "send":
                        break;

                    case "help":
                        this.out.writeUTF("""
                                Available commands:
                                login - login to server
                                passwd - change password (requires login)
                                cd - changes directory. cd [path]
                                """

                        );
                        break;

                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }

        } catch (EOFException e) {
            if (this.user != null)
                System.out.printf("Client %d -> %s closed connection\n", this.threadNumber, this.user.username); // Client
                                                                                                                 // closed
                                                                                                                 // socket
            else
                System.out.printf("Client %d closed connection\n", this.threadNumber); // Client closed socket
        } catch (IOException e) {
            System.out.println("IO:" + e);
        }

    }

    public void login() {
        String username = "", password = "";
        ArrayList<String> credentials;
        int index = 0;
        this.fh = new FileHandler();

        try {
            this.out.writeUTF("username: ");
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

            // Change to last directory after login

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
            // Check if user is logged in first
            if (this.user == null) {
                this.out.writeUTF("Login is required");
                return;
            }

            if (newDirectory.equals(""))
                newDirectory = "home";
            
            if (new File("server/users/" + this.user.username + "/" + newDirectory).exists()) {
                this.user.currentDirectory = newDirectory;
                config = this.fh.readFile("server/users/" + this.user.username + "/.config");
                config.set(2, newDirectory);
                this.fh.reWriteFile("server/users/" + this.user.username + "/.config", config);

                // send empty message for client because it's waiting for a message
                this.out.writeUTF("");
            } else {
                this.out.writeUTF("Directory doesn't exist");
            }

        } catch (IOException e) {
            System.out.println("Erro changing directory");
        }
    }

    public void listDirectory() {
        try {
            // Check if user is logged in first
            if (this.user == null) {
                this.out.writeUTF("Login is required");
                return;
            }

            StringBuilder result = new StringBuilder();

            File folder = new File("server/users/" + this.user.username + "/" + this.user.currentDirectory);
            File[] files = folder.listFiles();

            for (File file : files) {
                if (file.isDirectory()) {
                    //System.out.println("*" + file.getName() + "*");
                    result.append("*" + file.getName() + "*");
                } else {
                    //System.out.println(file.getName());
                    result.append(file.getName());
                }
                result.append("\t");
            }

            this.out.writeUTF(result.toString());

        } catch (IOException e) {
            System.out.println("Erro listing directory files");
        }
    }

}