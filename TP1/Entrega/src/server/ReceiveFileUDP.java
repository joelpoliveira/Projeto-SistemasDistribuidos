package server;

import java.io.*;
import java.net.*;

public class ReceiveFileUDP implements Runnable{
    private Thread t;
    private File f;
    private String path;
    private int port;

    public ReceiveFileUDP(String serverPath, int port){
        this.port = port;
        this.path = serverPath;
        this.t = new Thread(this, "FileReceiveUDP");
        this.t.start();
    }

    private static void sendAck(int foundLast, DatagramSocket socket, InetAddress hostname, int port)
            throws IOException {
        // Send acknowledgement
        byte[] ack = new byte[2];
        ack[0] = (byte) (foundLast >> 8);
        ack[1] = (byte) (foundLast);

        DatagramPacket ackPacket = new DatagramPacket(ack, ack.length, hostname, port);
        socket.send(ackPacket);
        //System.out.println("Sent ack: Sequence Number = " + foundLast);
    }

    private File createFile(DatagramSocket socket) throws IOException {
        socket.setSoTimeout(10000);
        byte[] filename = new byte[1024];
        File f = null;

        // Receive filename from packet
        DatagramPacket filenamePacket = new DatagramPacket(filename, filename.length);
        socket.receive(filenamePacket);
        // System.out.println("Received filename");

        byte[] data = filenamePacket.getData(); // Reading the name in bytes
        String filenameString = new String(data, 0, filenamePacket.getLength()); // Converting the name to string

        // Create file
        // System.out.println("Creating file");
        //this.serverPath + "users/" + this.username + "/home/" + this.filePath
        System.out.println(this.path + filenameString);
        f = new File(this.path + filenameString);
        // FileOutputStream fos = new FileOutputStream(f); // Creating the stream to
        // write the file/

        return f;
    }

    private static void receiveFile(File file, DatagramSocket socket) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);

        boolean EOF; // Have we reached end of file
        int sequenceNumber = 0; // Order of sequences
        int foundLast = 0; // The las sequence found

        while (true) {
            byte[] message = new byte[1024]; // Where the data from the received datagram is stored
            byte[] fileByteArray = new byte[1021]; // Where we store the data to be writen to the file

            // Receive packet and retrieve the data
            DatagramPacket receivedPacket = new DatagramPacket(message, message.length);
            socket.receive(receivedPacket);
            message = receivedPacket.getData(); // Data to be written to the file

            // Get port and hostname for sending acknowledgment
            InetAddress hostname = receivedPacket.getAddress();
            int port = receivedPacket.getPort();

            // Retrieve sequence number
            sequenceNumber = ((message[0] & 0xff) << 8) + (message[1] & 0xff);
            // Check if we reached last datagram (end of file)
            EOF = (message[2] & 0xff) == 1;

            // If sequence number is the last seen + 1, then it is correct
            // We get the data from the message and write the ack that it has been received
            // correctly
            if (sequenceNumber == (foundLast + 1)) {

                // set the last sequence number to be the one we just received
                foundLast = sequenceNumber;

                // Retrieve data from message
                System.arraycopy(message, 3, fileByteArray, 0, 1021);

                // Write the retrieved data to the file and print received data sequence number
                for (byte b: fileByteArray){
                    if (b != 0)
                        fos.write(b);
                    else
                        break;
                }

                //System.out.println("Received: Sequence number:" + foundLast);

                // Send acknowledgement
                sendAck(foundLast, socket, hostname, port);
            } else {
                //System.out.println("Expected sequence number: " + (foundLast + 1) + " but received " + sequenceNumber
                //        + ". DISCARDING");
                // Re send the acknowledgement
                sendAck(foundLast, socket, hostname, port);
            }
            // Check for last datagram
            if (EOF) {
                System.out.println("File " + file.getName() + " syncronized");
                fos.close();
                break;
            }
        }
    }

    public void run(){
        try(DatagramSocket socket = new DatagramSocket(0)){
            InetAddress hostname = InetAddress.getByName("localhost");
            byte this_port[] = Integer.toString(socket.getLocalPort()).getBytes();
            DatagramPacket port_packet= new DatagramPacket(this_port, this_port.length, hostname, this.port);
            socket.send(port_packet);

            byte mess[] = "Ok".getBytes();
            DatagramPacket packet= new DatagramPacket(mess, mess.length, hostname, this.port);
            socket.send(packet);

            File f = createFile(socket);
            receiveFile(f, socket);


        } catch (IOException e){
          System.out.println("Udp receiver IO:" + e.getMessage());  
        } 
    }
}
