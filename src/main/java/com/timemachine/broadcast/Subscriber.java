package com.timemachine.broadcast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Subscriber extends Thread {

    DatagramChannel channel;

    public Subscriber(DatagramChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        ByteBuffer buf = ByteBuffer.allocate(48);
        while (true) {
            try {
                buf.clear();
                channel.receive(buf);
                System.out.println("Receive" + new String(buf.array()));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
