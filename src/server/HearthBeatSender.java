package server;

import java.io.*;
import java.net.*;

public class HearthBeatSender implements Runnable {
    private int timeout;
    private int interval;
    private int maxFailedHeartBeats;
    private int bufferSize;
    private int failedHeartBeats;
    Thread t;

    public HearthBeatSender() {
        this.timeout = 5000;
        this.interval = 100;
        this.maxFailedHeartBeats = 5;
        this.bufferSize = 4096;
        this.failedHeartBeats = 0;
        this.t = new Thread(this, "HearthBeatSender");

        this.t.start();
    }

    public void run() {

        try (DatagramSocket ds = new DatagramSocket()) {
            ds.setSoTimeout(timeout); // Set socket timeout

            int count = 0;
            int failedheartbeats = 0;

            while (failedheartbeats < maxFailedHeartBeats) {
                InetAddress hostname = InetAddress.getByName("localhost");

                try {
                    // Create output stream as ByteArray
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(bos);

                    // write count and increment
                    dos.writeInt(count++);

                    byte[] buf = bos.toByteArray();

                    // Create sending UDP packet
                    DatagramPacket dp = new DatagramPacket(buf, buf.length, hostname, 8000);
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

                    System.out.println("Got: " + n + ".");
                } catch (SocketTimeoutException ste) {
                    failedHeartBeats++;
                    System.out.println("Failed heartbeats: " + failedHeartBeats);
                }
                Thread.sleep(interval);
            }
        } catch (IOException e) {
            System.out.println("ERRRO");
        } catch (InterruptedException e) {
            System.out.println("Thread.sleep error"); // Execption from Thread.sleep
        }

    }
}
