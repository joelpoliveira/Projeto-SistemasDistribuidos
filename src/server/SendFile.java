package server;

import java.net.*;
import java.io.*;

// Send file to client
public class SendFile implements Runnable {
    Socket downloadSocket;
    int threadNumber;
    Thread t;

    public SendFile(Socket downloadSocket, int threadNumber) {
        this.threadNumber = threadNumber;
        this.downloadSocket = downloadSocket;
        this.t = new Thread(this, Integer.toString(threadNumber));

        this.t.start();
    }

    public void run() {

    }

}
