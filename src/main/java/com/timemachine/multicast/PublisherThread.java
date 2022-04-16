package com.timemachine.multicast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static com.timemachine.multicast.MulticastApp.*;

public class PublisherThread extends Thread {

    private final InetSocketAddress inetSocketAddress;
    private final DatagramChannel datagramChannel;
    private final String nodeName;
    private final long frequency;

    public PublisherThread(String nodeName, long frequency) throws IOException {
        this.nodeName = nodeName;
        this.frequency = frequency;

        datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET);
        datagramChannel.bind(null);

        NetworkInterface networkInterface = NetworkInterface.getByName(MULTICAST_INTERFACE);
        datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
        inetSocketAddress = new InetSocketAddress(MULTICAST_IP, MULTICAST_PORT);
    }

    public void sendMessage(String message) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
        datagramChannel.send(byteBuffer, inetSocketAddress);
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.sendMessage(String.format("%s: Hi there, my name is %s", System.currentTimeMillis(), nodeName));
                Thread.sleep(frequency);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
