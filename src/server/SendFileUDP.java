package server;

import java.io.*;
import java.net.*;
// import java.net.*;

// TODO check if file is not a directory no TCP

public class SendFileUDP implements Runnable {
    String filePath;
    String username;
    //int length;
    int port;

    public SendFileUDP(String filePath, String username) {
        this.filePath = filePath;
        this.port = 8005;
        this.username = username;
        //this.length = length;

        new Thread(this, "FileSenderUDP").start();
    }

    private File getFile() {
        File file = new File("server/users/" + this.username + "/home/" + this.filePath);
        if (file.isFile()) 
            return file;
        else
            return null;
    }

    private byte[] fileToByteArray(File f) {
        FileInputStream fis;
        // Creating a byte array using the length of the file
        System.out.println((int) f.length());
        byte[] byteArray = new byte[(int) f.length()];

        try {
            fis = new FileInputStream(f);
            fis.read(byteArray);
            fis.close();

        } catch (IOException e) {
            System.err.println("Erro reading file " + e.getMessage());
        }
        return byteArray;
    }

    private void sendFilename(DatagramSocket socket, InetAddress hostname, File f) throws IOException {
        String filename;

        try {
            filename = f.getName();
            byte[] fileNameBytes = filename.getBytes(); // filename as bytes

            System.out.println("++++++" + filename);

            // Create packet
            DatagramPacket filenamePacket = new DatagramPacket(fileNameBytes, fileNameBytes.length, hostname, port);
            socket.send(filenamePacket); // Send the packet
        } catch (NullPointerException e) { // null if it's not a file
            System.out.println("Can only send files :) ");
        }
    }

    private void sendFile(DatagramSocket socket, byte[] fileByteArray, InetAddress hostname, int port) throws IOException{
        int packetNumber = 0; // To grantee packet order and not resend wrong packets
        boolean EOF = false; // To check for
        int ackSequence = 0; // To check if the datagram was received sucessfully
        boolean ackRec; // To check if the datagram was received

        for (int i = 0; i < fileByteArray.length; i += 1021) {
            packetNumber += 1;

            // Create message
            // First two bytes of the data are used for control purposes (datagram integrity and order)
            byte[] message = new byte[1024];
            message[0] = (byte) (packetNumber >> 8);
            message[1] = (byte) (packetNumber);

            if ((i + 1021) >= fileByteArray.length) {
                // We reached the end of the file (last datagram to be send)
                EOF = true;
                message[2] = (byte) (1);
            } else {
                // We haven't reached the end of the file, still sending datagrams
                message[2] = (byte) (0); 
            }

            if (!EOF) {
                // If it's not the last datagram, copy read bytes to message
                System.arraycopy(fileByteArray, i, message, 3, 1021);
            } else { 
                // If it's the last datagram
                System.arraycopy(fileByteArray, i, message, 3, fileByteArray.length - i);
            }

            // Send the packet number read
            DatagramPacket sendPacket = new DatagramPacket(message, message.length, hostname, port);
            socket.send(sendPacket); 

            System.out.println("Sent: Sequence number = " + packetNumber);

            // Receive ACK from receiver
            while (true) {
                byte[] ack = new byte[2]; // Create packet for datagram ackknowledgement
                DatagramPacket ackPack = new DatagramPacket(ack, ack.length);

                try {
                    socket.setSoTimeout(50);
                    // Receive ack
                    socket.receive(ackPack);
                    // packet number from ack
                    ackSequence = ((ack[0] & 0xff) << 8) + (ack[1] & 0xff); // Figuring the sequence number
                    ackRec = true; // ACK was received
                } catch (SocketTimeoutException e) {
                    System.out.println("Socket timed out waiting for ack");
                    ackRec = false; // Din't receive an ack in time
                }

                // If the package was received correctly next packet can be sent
                if ((ackSequence == packetNumber) && ackRec) {
                    System.out.println("Ack received: Sequence Number = " + ackSequence);
                    break;
                }
                else {
                    // Packet was not received -> resend it
                    socket.send(sendPacket);
                    System.out.println("Resending: Sequence Number = " + packetNumber);
                }
            }
        }
    }

    public void run() {

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress hostname = InetAddress.getByName("localhost");
            // Get the file
            File f = getFile();

            // Send filename to receiver
            sendFilename(socket, hostname, f);

            // Send file
            // Convert file to byte array
            byte[] fileByteArray = fileToByteArray(f); // Array of bytes the file is made of

            // Send file
            sendFile(socket, fileByteArray, hostname, port);

            socket.close();
        } catch (IOException e) {
            System.out.println("Erro replicating file" + e.getMessage());
        }
    }

}
