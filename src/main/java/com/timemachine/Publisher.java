package com.timemachine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static com.timemachine.Config.*;

public class Publisher {

    private InetSocketAddress inetSocketAddress;
    private DatagramChannel datagramChannel;
    private String nodeName;

    public Publisher(String nodeName) throws IOException {
        this.nodeName = nodeName;
        datagramChannel = DatagramChannel.open();
        datagramChannel.bind(null);

        NetworkInterface networkInterface = NetworkInterface.getByName(MULTICAST_INTERFACE);
        datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
        inetSocketAddress = new InetSocketAddress(MULTICAST_IP, MULTICAST_PORT);
    }

    public void sendMessage(String message) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
        datagramChannel.send(byteBuffer, inetSocketAddress);
    }

    public static class SubThread extends Thread {
        String threadName;
        Subscriber mc;

        public SubThread(Subscriber mc, String name) {
            this.mc = mc;
            this.threadName = name;
        }

        @Override
        public void run() {
            this.setName(threadName);
            try {
                while(true) {
                    mc.receiveMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
                mc.membershipKey.drop();
            }
        }
    }


    public static class PubThread extends Thread {
        String threadName;
        Publisher mp;
        long frequency;

        public PubThread(Publisher mp, String name, long frequency) {
            this.mp = mp;
            this.threadName = name;
            this.frequency = frequency;
        }

        @Override
        public void run() {
            this.setName(threadName);
            try {
                while(true) {
                    mp.sendMessage(String.format("%s: %s %s", mp.nodeName, System.currentTimeMillis(), "Hi There"));
                    Thread.sleep(frequency);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
