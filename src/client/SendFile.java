package client;

import java.net.*;
import java.io.*;

public class SendFile implements Runnable {
    Socket downloadSocket;
    int threadNumber;
    Thread t;
    DataInputStream in;
    DataOutputStream out;
    String hostname;

    public SendFile(String hostname) {
        this.hostname = hostname;
        this.in = in;
        this.out = out;
        this.t = new Thread(this, "clientFileSender");

        this.t.start();
    }

    public void run() {
        int downloadPort = 0;

        try {
            // Receive connection port
            downloadPort = Integer.parseInt(this.in.readUTF());
            // System.out.println(downloadPort);

            if (downloadPort == -1) {
                System.out.println("Login is required");
                return;
            }
        } catch (IOException e) {
            System.out.println("Failed to read port");
        }

        // Create socket and received port
        try (Socket downloadSocket = new Socket(this.hostname, downloadPort)) {
            System.out.println("Ready do send file");

            byte[] contents; // arary of bytes read

            OutputStream os = downloadSocket.getOutputStream();

            File file = new File("client/file");
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fin);

            long fileLength = file.length();
            long current = 0; // curent number of bytes read
            int size = 5; // number of bytes to send at once

            while (current != fileLength) {
                if (fileLength - current >= size)
                    current += size;
                else {
                    size = (int) (fileLength - current);
                    current = fileLength;
                }
                contents = new byte[size];
                bis.read(contents, 0, size);
                os.write(contents);
                os.flush();
                // System.out.print("Sending file ... " + (current * 100) / fileLength + "%
                // complete!\n");
            }

            // File transfer completed
            fin.close();
            bis.close();
        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage()); // Server socket not open anymore
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage()); // Connection refused. Port not open on
                                                        // hostname
        }

    }
}
