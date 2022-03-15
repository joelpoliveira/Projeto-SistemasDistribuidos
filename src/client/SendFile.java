package client;

import java.net.*;
import java.io.*;

public class SendFile implements Runnable {
    Socket downloadSocket;
    int threadNumber;
    Thread t;
    //DataInputStream in;
    //DataOutputStream out;
    String hostname;
    int downloadPort;
    String sourcePath;
    //String destinationPath;
    String username;
    int size;

    public SendFile(String username, String hostname, int port, String source) {
        this.hostname = hostname;
        this.downloadPort = port;
        this.sourcePath = source;
        this.username = username;
        this.size = 5; // Number of bytes to send at once
        
        // nome da thread pode dar problemas se tentarmos mandar varios ficheiros
        this.t = new Thread(this, "clientFileSender");

        this.t.start();
    }

    public void run() {

        // System.out.println("Created thread to send file");

        // Create socket with received port from server
        try (Socket downloadSocket = new Socket(this.hostname, this.downloadPort)) {

            byte[] contents; // arary of bytes read

            OutputStream os = downloadSocket.getOutputStream();

            File file = new File("client/users/" + this.username + "/home/" + this.sourcePath);
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fin);

            long fileLength = file.length();
            long current = 0; // curent number of bytes read

            // System.out.println("Sending file...");

            while (current != fileLength) {
                if (fileLength - current >= this.size)
                    current += this.size;
                else {
                    size = (int) (fileLength - current);
                    current = fileLength;
                }
                contents = new byte[this.size];
                bis.read(contents, 0, this.size);
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
