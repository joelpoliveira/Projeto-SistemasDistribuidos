package server;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class HearthBeatSender implements Runnable {
    private int timeout;
    private int interval;
    private int maxFailedHeartBeats;
    private int bufferSize;
    private int failedHeartBeats;
    private int PORT;
    private Server caller;
    Thread t;

    public HearthBeatSender(Server lock, HashMap<String, String> confs) {
        this.timeout = Integer.parseInt(confs.get("timeout"));
        this.interval = Integer.parseInt(confs.get("interval"));
        
        this.bufferSize = 4096;

        this.maxFailedHeartBeats = Integer.parseInt(confs.get("maxFailedHeartBeats"));
        this.failedHeartBeats = 0;

        this.PORT = Integer.parseInt(confs.get("heartBeatPort"));

        this.caller = lock;
        this.t = new Thread(this, "HearthBeatSender");
        this.t.start();
    }

    public void run() {

        try (DatagramSocket ds = new DatagramSocket()) {
            ds.setSoTimeout(timeout); // Set socket timeout

            int count = 0;
            
            while (failedHeartBeats < maxFailedHeartBeats) {
                InetAddress hostname = InetAddress.getByName("localhost");

                try {
                    // Create output stream as ByteArray
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(bos);

                    // write count and increment
                    dos.writeInt(count++);

                    byte[] buf = bos.toByteArray();

                    // Create sending UDP packet
                    DatagramPacket dp = new DatagramPacket(buf, buf.length, hostname, this.PORT);
                    ds.send(dp); // send UDP packet

                    // Create receiving UDP packet
                    byte[] rbuf = new byte[bufferSize];
                    DatagramPacket dr = new DatagramPacket(rbuf, rbuf.length);

                    ds.receive(dr); // Receive UDP packet

                    failedHeartBeats = 0; // reset failed hearth beats

                    //
                    ByteArrayInputStream bais = new ByteArrayInputStream(rbuf, 0, dr.getLength());
                    DataInputStream dis = new DataInputStream(bais);

                    int n = dis.readInt();

                    //System.out.println("Got: " + n + ".");
                } catch (SocketTimeoutException ste) {
                    failedHeartBeats++;
                    System.out.println("Failed heartbeats: " + failedHeartBeats);
                }
                Thread.sleep(interval);
            }

            //release second server;
            synchronized (this.caller){
                this.caller.notify();
            }
            ds.close();
        } catch (IOException e) {
            System.out.println("ERRRO");
        } catch (InterruptedException e) {
            System.out.println("Thread.sleep error"); // Execption from Thread.sleep
        }
        this.t.interrupt();
        System.out.println("Sender Out");

    }
}
