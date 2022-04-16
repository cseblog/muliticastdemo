package com.timemachine.multicast;

import java.io.IOException;

public class MulticastApp {
    public static final String MULTICAST_INTERFACE = "en0";
    public static final int MULTICAST_PORT = 4321;
    public static final String MULTICAST_IP = "239.255.255.1";

    public static void main(String[] args) throws IOException {
        PublisherThread publisher = new PublisherThread(args[0], Long.parseLong(args[1]));
        SubscriberThread subscriber = new SubscriberThread(args[0]);

        publisher.start();
        subscriber.start();
    }
}
