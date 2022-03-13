package server;

import common.*;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class DownloadConnection implements Runnable {
    DataInputStream in;
    DataOutputStream out;
    FileInputStream fin;
    FileOutputStream fout;
    Socket downloadSocket;
    int threadNumber;
    Thread t;

    public DownloadConnection(Socket downloadSocket, int threadNumber) {
        this.threadNumber = threadNumber;
        //try {
            this.downloadSocket = downloadSocket;
            //this.in = new DataInputStream(downloadSocket.getInputStream());
            //this.out = new DataOutputStream(downloadSocket.getOutputStream());
            this.t = new Thread(this, Integer.toString(threadNumber));
        //} catch (IOException e) {
        //    System.out.println("Connection:" + e.getMessage());
       // }

        this.t.start();
    }

    public void run() {
        System.out.printf("Download thread %d ready!\n", this.threadNumber);
        try {
            byte[] contents = new byte[100];
            // Initialize the FileOutputStream to the output file's full path.
            FileOutputStream fos = new FileOutputStream("server/teste");
            InputStream is = this.downloadSocket.getInputStream();
            // No of bytes read in one read() call
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
