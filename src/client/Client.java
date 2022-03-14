package client;

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

        // Create socket
        try (Socket s = new Socket(args[0], socketPort)) {
            System.out.println("Created socket = " + s);

            // Create input and output DataStreams
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String text = "";
            String[] temp;

            try {
                Scanner sc = new Scanner(System.in);

                while (true) {
                    System.out.print(in.readUTF() + "> ");
                    text = sc.nextLine();

                    if (!text.equals(""))
                        out.writeUTF(text);

                    temp = text.split(" ");

                    switch (temp[0]) {
                        case "login":
                            // username
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());
                            // password
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());

                            // Server message
                            System.out.println(in.readUTF());
                            break;

                        case "passwd":
                            // Password 1
                            // check if server asks for password. If not, user is not logged in
                            if (!(text = in.readUTF()).equals("New password: ")) {
                                System.out.println(text);
                                break;
                            }
                            System.out.print(text);
                            out.writeUTF(sc.nextLine());
                            // Password 2
                            System.out.print(in.readUTF());
                            out.writeUTF(sc.nextLine());

                            // Server message
                            System.out.println(in.readUTF());
                            break;

                        case "cd":
                            // Server message
                            if (!((text = in.readUTF()).equals("")))
                                System.out.println(text);
                            break;

                        case "ls":
                            // Server message
                            System.out.println(in.readUTF());
                            break;

                        case "send":
                            // Receive connection port
                            int downloadPort = Integer.parseInt(in.readUTF());
                            //System.out.println(downloadPort);

                            if (downloadPort == -1){
                                System.out.println("Login is required");
                                break;
                            }
                            
                            // Create socket and received port
                            try (Socket downloadSocket = new Socket(args[0], downloadPort)) {
                                System.out.println("Ready do send file");

                                byte[] contents; // arary of bytes read

                                OutputStream os = downloadSocket.getOutputStream();

                                File file = new File("client/test");
                                FileInputStream fin = new FileInputStream(file);
                                BufferedInputStream bis = new BufferedInputStream(fin);

                                long fileLength = file.length();
                                long current = 0; // curent number of bytes read
                                int size = 100; // number of bytes to send at once

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
                                    System.out.print("Sending file ... " + (current * 100) / fileLength + "% complete!\n");
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
                            break;
                        
                        case "download":
                            break;

                        case "help":
                            System.out.println(in.readUTF());
                            break;

                        default:
                            break;
                    }
                }

            } catch (EOFException e) {
                System.out.println("Server closed connection"); // Server closed socket
                // TODO connect to secondary server
            } catch (IOException e) {
                System.out.println("IO:" + e);
            }

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage()); // Server socket not open anymore
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage()); // Connection refused. Port not open on hostname
        }
    }
    
}
