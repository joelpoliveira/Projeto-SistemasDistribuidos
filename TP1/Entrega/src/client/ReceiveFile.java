package client;

import java.net.*;
import java.io.*;

public class ReceiveFile implements Runnable {
    // Socket downloadSocket;
    // int threadNumber;
    Thread t;
    String destinationPath;
    String username;
    int size;
    String hostname;
    int downloadPort;

    public ReceiveFile(String username, String hostname, int downloadPort, String destinationPath) {
        // this.threadNumber = threadNumber;
        this.destinationPath = destinationPath;
        this.username = username;
        this.hostname = hostname;
        this.downloadPort = downloadPort;
        this.t = new Thread(this, "clientFileReceiver");
        this.size = 5;

        this.t.start();
    }

    public void run() {
        //System.out.println("Receiver thread ready!");

        try (Socket downloadSocket = new Socket(this.hostname, this.downloadPort)) {
            byte[] contents = new byte[this.size];

            // Initialize the FileOutputStream to the output file's full path.
            FileOutputStream fos = new FileOutputStream("client/users/" + this.username + "/home/" + this.destinationPath);
            InputStream is = downloadSocket.getInputStream();

            // Number of bytes read in one read() cicle
            int bytesRead = 0;
            while ((bytesRead = is.read(contents)) != -1){
                fos.write(contents, 0, bytesRead);
            }
            
            System.out.println("File saved successfully to" + this.username + "/home/" + this.destinationPath);
            fos.close();
            downloadSocket.close();

        } catch (IOException e) {
            System.out.println("Download" + e.getStackTrace());
        }

    }
    
}
