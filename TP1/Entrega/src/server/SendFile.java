package server;

import java.net.*;
import java.io.*;

// Send file to client
public class SendFile implements Runnable {
    Socket downloadSocket;
    int threadNumber;
    Thread t;
    String serverPath;
    String sourcePath;
    // String destinationPath;
    String username;
    int size;

    public SendFile(Socket downloadSocket, String sourcePath, String username, String serverPath) {

        this.downloadSocket = downloadSocket;
        this.sourcePath = sourcePath;
        this.username = username;
        this.size = 5; // Number of bytes to send at once
        this.serverPath = serverPath;

        this.t = new Thread(this, "serverFileSender");

        this.t.start();
    }

    public void run() {
        System.out.println("Sender thread ready!");

        try {
            byte[] contents; // arary of bytes read

            OutputStream os = this.downloadSocket.getOutputStream();

            File file = new File(this.serverPath + "users/" + this.username + "/home/" + this.sourcePath);
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fin);

            long fileLength = file.length();
            long current = 0; // curent number of bytes read

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
            }

            System.out.printf("Client %s sucessfuly downloaded %s\n", this.username, this.sourcePath);

            // File transfer completed
            fin.close();
            bis.close();

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        }

    }

}
