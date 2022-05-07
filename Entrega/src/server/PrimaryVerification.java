package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class PrimaryVerification implements Runnable{
    private HashMap<String, String> confs;
    private String strg_info;
    Thread t;

    public PrimaryVerification( HashMap<String, String> confs, String storage){
        this.strg_info = storage;
        this.confs = confs;
        this.t = new Thread(this);
        this.t.start();
    }

    public void run(){
        try (DatagramSocket socket = new DatagramSocket( Integer.parseInt( confs.get("checkPort") ) ) ) {
            while ( !this.t.isInterrupted() ) {
                
                byte buf[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                
                socket.receive(packet);

                String message=new String(packet.getData(),
                                        packet.getOffset(),
                                        packet.getLength(),
                                        StandardCharsets.UTF_8);

                //System.out.println("[MESSAGE RECEIVED] " + message);

                if (message.equals("MAIN WAKED?")){
                    byte resp[] = this.strg_info.getBytes();
                    DatagramPacket respPacket = new DatagramPacket(resp, resp.length, packet.getAddress(), packet.getPort());
                    socket.send(respPacket);
                }                
            }
            socket.close();
            
        } catch (IOException e) {
            System.out.println("Erro DatagramSocket verification receiver");
        }
    }

    public void interrupt(){
        this.t.interrupt();
    }
}
