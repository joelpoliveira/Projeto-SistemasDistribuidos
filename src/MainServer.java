import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class MainServer {
    public static void main(String[] args) {
        int numero = 0;
        int serverPort = 0;

        try {
            serverPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
            System.out.println("Main RMI server started");
            System.out.println("Created listen socket = " + listenSocket);
            while (true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("Created client socket (created at accept())=" + clientSocket);
                numero++;
                new Connection(clientSocket, numero);
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }
    }
}

// Thread para tratar de cada canal de comunicação com um cliente
class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;

    public Connection(Socket aClientSocket, int numero) {
        thread_number = numero;
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    // =============================
    public void run() {
        String resposta = "", username = "", password = "";

        try {
            out.writeUTF("username: ");
            username = in.readUTF();
            out.writeUTF("password: ");
            password = in.readUTF();    

        } catch (EOFException e) {
            System.out.println("EOF:" + e); // Client closed socket
        } catch (IOException e) {
            System.out.println("IO:" + e);
        }



        // try {
        //     while (true) {
        //         // an echo server
        //         String data = in.readUTF();
        //         System.out.println("T[" + thread_number + "] Recebeu: " + data);
        //         resposta = data.toUpperCase();
        //         out.writeUTF(resposta);
        //     }
        // } catch (EOFException e) {
        //     System.out.println("EOF:" + e); // Client closed socket
        // } catch (IOException e) {
        //     System.out.println("IO:" + e);
        // }
    }
}