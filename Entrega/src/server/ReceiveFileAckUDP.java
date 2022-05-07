package server;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ReceiveFileAckUDP implements Runnable {
    Thread t;
    int PORT;
    String path;
    
    public ReceiveFileAckUDP(int port, String serverPath) {
        this.PORT = port;
        this.path = serverPath;
        this.t = new Thread(this, "FileAckReceiverUDp");
        this.t.start();
    }

    public void run() {
        try (DatagramSocket socket = new DatagramSocket(this.PORT)) {
            socket.setSoTimeout(3000);
            while(!this.t.isInterrupted()){
                // Create file
                try{
                    byte port[] = new byte[1024];
                    DatagramPacket port_packet = new DatagramPacket(port, port.length);
                    socket.receive(port_packet);
                    System.out.println("File request received");
                    new ReceiveFileUDP( this.path, Integer.parseInt(new String( port_packet.getData(),
                                                                     port_packet.getOffset(),
                                                                     port_packet.getLength(),
                                                                     StandardCharsets.UTF_8) ));
                }catch(SocketTimeoutException te){
                    continue;
                }
                //receiveFile(f, socket);
            }

            System.out.println("Closed " + this.PORT + " Port");
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Erro DatagramSocket receiver");
        }
    }

    public void interrupt(){
        System.out.println("Interruption Called");
        this.t.interrupt();
        
    }
}
