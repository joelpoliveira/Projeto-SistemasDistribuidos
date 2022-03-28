package server;

import java.io.*;
import java.net.*;

public class HearthBeatReceiver implements Runnable {
    private int bufferSize;
    private int PORT;
    Thread t;

    public HearthBeatReceiver() {
        this.bufferSize = 4096;
        this.PORT = 8005;

        this.t = new Thread(this, "HearthBeatReceiver");
        this.t.start();
    }

    public void run() {
        try (DatagramSocket ds = new DatagramSocket(this.PORT)) {

            while ( !this.t.isInterrupted() ) {

                byte buf[] = new byte[bufferSize];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                
                ds.receive(dp);
                ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
                DataInputStream dis = new DataInputStream(bais);
                int count = dis.readInt();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeInt(count);
                byte resp[] = baos.toByteArray();
                DatagramPacket dpresp = new DatagramPacket(resp, resp.length, dp.getAddress(), dp.getPort());
                ds.send(dpresp);
            }

        } catch (IOException e) {
            System.out.println("Erro");
        }
    }

    public void interrupt(){
        this.t.interrupt();
    }

}
