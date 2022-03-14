package server;

import java.net.*;
import java.io.*;

// Receive file from a client
public class ReceiveFile implements Runnable {
    Socket downloadSocket;
    int threadNumber;
    Thread t;

    public ReceiveFile(Socket downloadSocket, int threadNumber) {
        this.threadNumber = threadNumber;
        this.downloadSocket = downloadSocket;
        this.t = new Thread(this, Integer.toString(threadNumber));

        this.t.start();
    }

    public void run() {
        System.out.printf("Download thread %d ready!\n", this.threadNumber);
        try {
            byte[] contents = new byte[5];

            // Initialize the FileOutputStream to the output file's full path.
            FileOutputStream fos = new FileOutputStream("server/teste");
            InputStream is = this.downloadSocket.getInputStream();

            // Number of bytes read in one read() cicle
            int bytesRead = 0;
            while ((bytesRead = is.read(contents)) != -1){
                fos.write(contents, 0, bytesRead);
            }
            
            System.out.println("File saved successfully!");
            fos.close();
            this.downloadSocket.close();

        } catch (IOException e) {
            System.out.println("Download" + e.getStackTrace());
        }

    }

}
