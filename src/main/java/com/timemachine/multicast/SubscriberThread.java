package com.timemachine.multicast;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

import static com.timemachine.multicast.MulticastApp.*;

public class SubscriberThread extends Thread {

    private final DatagramChannel datagramChannel;
    private final MembershipKey membershipKey;
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private final String name;

    public SubscriberThread(String name) throws IOException {
        NetworkInterface networkInterface = NetworkInterface.getByName(MULTICAST_INTERFACE);
        InetAddress inetAddress = InetAddress.getByName(MULTICAST_IP);
        datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET);
        datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        datagramChannel.bind(new InetSocketAddress(MULTICAST_PORT));
        datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);

        membershipKey = datagramChannel.join(inetAddress, networkInterface);
        this.name = name;
    }

    public void receiveMessage() throws IOException {
        byteBuffer.clear();
        datagramChannel.receive(byteBuffer);
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes, 0, byteBuffer.limit());
        System.out.println("Received:  " + new String(bytes));
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.receiveMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.membershipKey.drop();
        }
    }
}

