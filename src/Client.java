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

        String texto = "", data = "";

        // Create socket
        try (Socket s = new Socket(args[0], socketPort)) {
            System.out.println("Created socket = " + s);

            // Create input and output DataStreams
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            
            // ==== AUTH ===
            try {
                try (Scanner sc = new Scanner(System.in)) {
                    //username
                    System.out.print(in.readUTF());
                    texto = sc.nextLine();
                    out.writeUTF(texto);
                    //password
                    System.out.print(in.readUTF());
                    texto = sc.nextLine();
                    out.writeUTF(texto);

                    //out.writeUTF()
                }

            } catch (EOFException e) {
                System.out.println("EOF:" + e); // Client closed socket
            } catch (IOException e) {
                System.out.println("IO:" + e);
            }

            // try (Scanner sc = new Scanner(System.in)) {
            //     while (true) {
            //         texto = sc.nextLine();
            //         out.writeUTF(texto);
            //         data = in.readUTF();
            //         System.out.println(">" + data);
            //     }
            // }

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage()); // Server socket not open anymore
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage()); // Connection refused. Port not open on hostname
        }
    }
}
