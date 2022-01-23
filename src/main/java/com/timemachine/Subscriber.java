package com.timemachine;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

import static com.timemachine.Config.*;

public class Subscriber {

    DatagramChannel datagramChannel;
    MembershipKey membershipKey;
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    public Subscriber() throws IOException {
        NetworkInterface networkInterface = NetworkInterface.getByName(MULTICAST_INTERFACE);
        InetAddress inetAddress = InetAddress.getByName(MULTICAST_IP);

        datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET);
        datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        datagramChannel.bind(new InetSocketAddress(MULTICAST_PORT));
        datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
        membershipKey = datagramChannel.join(inetAddress, networkInterface);
    }

    public void receiveMessage() throws IOException {
        byteBuffer.clear();
        datagramChannel.receive(byteBuffer);
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes, 0, byteBuffer.limit());
        System.out.println(new String(bytes));
    }
}

