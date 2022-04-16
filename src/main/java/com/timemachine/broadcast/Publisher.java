package com.timemachine.broadcast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Publisher extends Thread {
    InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 9999);
    DatagramChannel channel;

    public Publisher(DatagramChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        ByteBuffer buf = ByteBuffer.allocate(48);

        while (true) {
            try {
                String newData = "New String to write to file..." + System.currentTimeMillis();
                System.out.println(newData);

                buf.clear();
                buf.put(newData.getBytes());
                buf.flip();

                channel.send(buf, inetSocketAddress);
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
