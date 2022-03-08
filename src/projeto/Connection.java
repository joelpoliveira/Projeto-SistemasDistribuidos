package projeto;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;

public class Connection implements Runnable {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int threadNumber;
    Thread t;

    public Connection(Socket aClientSocket, int numero) {
        this.threadNumber = numero;
        try {
            this.clientSocket = aClientSocket;
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            this.t = new Thread(this, Integer.toString(threadNumber));
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }

        // Start Thread
        t.start();
    }

    // ================== Thread main
    public void run() {
        String text = "", username = "", password = "";

        try {
            FileHandler fh = new FileHandler("credentials");

            while (true) {
                text = in.readUTF();
                System.out.printf("Received %s from %d\n", text, threadNumber);

                switch (text) {
                    case "register":
                        out.writeUTF("username: ");
                        username = in.readUTF();
                        System.out.println(username);

                        out.writeUTF("password: ");
                        password = in.readUTF();
                        System.out.println(password);

                        // Create directory
                        try {
                            Path path = Paths.get("./users/" + username);
                            Files.createDirectories(path);
                        } catch (FileAlreadyExistsException e) {
                            System.out.println("User alredy registered");
                            out.writeUTF("User alredy registered");
                            break;
                        }

                        // Write username and password to file
                        fh.writeFile(username);
                        fh.writeFile(password);

                        out.writeUTF("User created");
                        break;

                    case "login":
                        out.writeUTF("username: ");
                        username = in.readUTF();
                        System.out.println(username);

                        out.writeUTF("password: ");
                        password = in.readUTF();
                        System.out.println(password);
                        break;

                    case "teste":
                        fh.readFile();
                        fh.writeFile("Teste");
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
}