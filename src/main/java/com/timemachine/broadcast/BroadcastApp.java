package com.timemachine.broadcast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class BroadcastApp {

    public static void main(String[] args) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress(9999));

        Publisher publisher = new Publisher(datagramChannel);
        Subscriber subscriber = new Subscriber(datagramChannel);

        publisher.start();
        subscriber.start();
    }
}
