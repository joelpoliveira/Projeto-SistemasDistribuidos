package projeto;

import java.net.*;
import java.util.HashMap;
import java.io.*;
import java.nio.file.*;

public class Connection implements Runnable {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int threadNumber;
    Thread t;
    // User info
    boolean loggedIn;
    String username;
    String password;

    public Connection(Socket aClientSocket, int numero) {
        this.threadNumber = numero;
        try {
            this.clientSocket = aClientSocket;
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            this.t = new Thread(this, Integer.toString(threadNumber));
            this.loggedIn = false;
            this.username = "";
            this.password = "";
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }

        // Start Thread
        t.start();
    }

    // ================== Thread main
    @SuppressWarnings("unchecked") // Supress warning related to object to Hashmap conversion. No other objects
                                   // will be read
    public void run() {
        String text = "";
        try {
            FileHandler fh = new FileHandler("credentials");

            while (true) {
                text = in.readUTF();
                System.out.printf("Received %s from %d\n", text, threadNumber);

                switch (text) {
                    case "register":
                        register(fh);
                        break;

                    case "login":
                        login();
                        break;
                    
                    case "change":
                        change_password();
                        break;

                    case "teste":
                        try {
                            ObjectInputStream i = new ObjectInputStream(new FileInputStream("credentials"));
                            HashMap<String, String> credentials_2;
                            credentials_2 = (HashMap<String, String>) i.readObject();
                            System.out.println(credentials_2);
                            i.close();

                        } catch (IOException e) {
                            System.err.println("Erro");
                        } catch (ClassNotFoundException e) {
                            System.err.println("Erro");
                        }
                        break;

                    default:
                        System.out.println("Entered default");
                        break;
                }
            }

        } catch (EOFException e) {
            System.out.printf("Client %d closed connection\n", this.threadNumber); // Client closed socket
        } catch (IOException e) {
            System.out.println("IO:" + e);
        }

    }

    @SuppressWarnings("unchecked")
    public synchronized void register(FileHandler fh) {
        String username = "", password = "";
        try {
            this.out.writeUTF("username: ");
            username = this.in.readUTF();
            System.out.println(username);

            this.out.writeUTF("password: ");
            password = this.in.readUTF();
            System.out.println(password);

            // Create user directory
            try {
                Path path = Paths.get("./users/" + username + "/home");
                Files.createDirectories(path);
            } catch (FileAlreadyExistsException e) {
                System.out.println("User alredy registered");
                out.writeUTF("User alredy registered");
                return;
            }

            // Create user config file
            try {
                FileHandler configFile = new FileHandler("./users/" + username + "/.config");
                configFile.writeFile("");
            } catch (Exception e) {
                System.out.println("Failed to create .config file");
                out.writeUTF("Failed to create config file. User not created");
                return;
            }

            // Write username and password to credentials file
            try {
                ObjectInputStream i = new ObjectInputStream(new FileInputStream("credentials"));
                HashMap<String, String> teste;

                teste = (HashMap<String, String>) i.readObject();
                teste.put(username, password);
                i.close();

                ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("credentials"));
                o.writeObject(teste);
                o.close();

            } catch (IOException e) {
                System.err.println("Erro");
            } catch (ClassNotFoundException e) {
                System.err.println("Erro");
            }

            // fh.writeFile(username);
            // fh.writeFile(password);

            out.writeUTF("User created");
        } catch (IOException e) {
            System.out.println("Error register");
        }

    }

    @SuppressWarnings("unchecked")
    public synchronized void login() {
        String username = "", password = "", temp = "";
        try {
            this.out.writeUTF("username: ");
            username = this.in.readUTF();
            // System.out.println(username);

            this.out.writeUTF("password: ");
            password = this.in.readUTF();
            // System.out.println(password);

            try {
                // Read credentials file
                ObjectInputStream i = new ObjectInputStream(new FileInputStream("credentials"));
                HashMap<String, String> teste;

                teste = (HashMap<String, String>) i.readObject();
                i.close();
                
                // Check if user is already logged in
                if (this.username.equals(username)) {
                    this.out.writeUTF("User alredy logged in");
                    return;
                }

                // Check username and password to authenticate
                if ((temp = teste.get(username)) == null) {
                    this.out.writeUTF("User doesnt exist");
                    return;
                } else {
                    if (password.equals(temp)) {
                        this.loggedIn = true;
                        this.username = username;
                        this.password = password;
                        this.out.writeUTF("Logged in");
                        return;
                    } else {
                        this.out.writeUTF("Wrong password");
                        return;
                    }
                }

            } catch (IOException e) {
                System.err.println("Failed to read credentials file");
            } catch (ClassNotFoundException e) {
                System.err.println("Erro");
            }

        } catch (IOException e) {
            System.out.println("Error login");
        }
    }

    @SuppressWarnings("unchecked")
    public void change_password() {
        String password1 = "", password2 = "";
        try {
            if (this.loggedIn){
                this.out.writeUTF("New password: ");
                password1 = this.in.readUTF();
                
                this.out.writeUTF("Repeat password: ");
                password2 = this.in.readUTF();

                if (password1.equals(password2)){
                    // mudar no hashmap
                    try {
                        ObjectInputStream i = new ObjectInputStream(new FileInputStream("credentials"));
                        HashMap<String, String> credentials;
        
                        credentials = (HashMap<String, String>) i.readObject();
                        credentials.replace(this.username, password1);
                        i.close();
        
                        ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("credentials"));
                        o.writeObject(credentials);
                        o.close();
        
                    } catch (IOException e) {
                        System.err.println("Failed to write credentials file");
                    } catch (ClassNotFoundException e) {
                        System.err.println("Erro");
                    }
                    

                    
                    this.password = password1;
                    this.out.writeUTF("Password changed");
                    return;
                    
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

}
