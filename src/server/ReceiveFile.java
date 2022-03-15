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

    public ReceiveFile(Socket downloadSocket, String destinationPath, String username) {
        // this.threadNumber = threadNumber;
        this.downloadSocket = downloadSocket;
        this.destinationPath = destinationPath;
        this.username = username;
        this.t = new Thread(this, "serverFileReceiver");
        this.size = 5;

        this.t.start();
    }

    public void run() {
        System.out.println("Receiver thread ready!");
        try {
            byte[] contents = new byte[this.size];

            // Initialize the FileOutputStream to the output file's full path.
            FileOutputStream fos = new FileOutputStream("server/users/" + this.username + "/home/" + this.destinationPath);
            InputStream is = this.downloadSocket.getInputStream();

            // Number of bytes read in one read() cicle
            int bytesRead = 0;
            while ((bytesRead = is.read(contents)) != -1){
                fos.write(contents, 0, bytesRead);
            }
            
            System.out.println("File saved successfully to" + this.username + "/home/" + this.destinationPath);
            fos.close();
            this.downloadSocket.close();

        } catch (IOException e) {
            System.out.println("Download" + e.getStackTrace());
        }

    }

}
