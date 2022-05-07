package server;

import java.net.*;
import java.io.*;

// Receive file from a client
public class ReceiveFile implements Runnable {
    Socket downloadSocket;
    // int threadNumber;
    Thread t;
    String destinationPath;
    String username;
    int size;
    int udp_port;
    String serverPath;

    public ReceiveFile(Socket downloadSocket, String destinationPath, String username, String serverPath, int udp_port) {
        // this.threadNumber = threadNumber;
        this.downloadSocket = downloadSocket;
        this.destinationPath = destinationPath;
        this.username = username;
        this.t = new Thread(this, "serverFileReceiver");
        this.size = 5;
        this.serverPath = serverPath;
        this.udp_port = udp_port;

        this.t.start();
    }

    public void run() {
        // int total = 0;
        System.out.println("Receiver thread ready!");
        try {
            byte[] contents = new byte[this.size];

            // Initialize the FileOutputStream to the output file's full path.
            FileOutputStream fos = new FileOutputStream(this.serverPath + "users/" + this.username + "/home/" + this.destinationPath);
            InputStream is = this.downloadSocket.getInputStream();

            // Number of bytes read in one read() cicle
            int bytesRead = 0;
            while ((bytesRead = is.read(contents)) != -1){
                fos.write(contents, 0, bytesRead);
                //total += bytesRead;
            }
            
            System.out.println("File saved successfully to " + this.username + "/home/" + this.destinationPath);
            fos.close();
            this.downloadSocket.close();
            
            // Replicate file on secondary server
            new SendFileUDP(this.destinationPath, this.username, this.serverPath, this.udp_port, false);

        } catch (IOException e) {
            System.out.println("Download " + e.getMessage());
        }

    }

}
