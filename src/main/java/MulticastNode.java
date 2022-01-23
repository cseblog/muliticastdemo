import com.timemachine.Publisher;
import com.timemachine.Subscriber;

import java.io.IOException;

public class MulticastNode {
    public static void main(String[] args) throws IOException {
        Publisher mp = new Publisher(args[0]);
        Publisher.PubThread pubThread = new Publisher.PubThread(mp, args[0], Long.parseLong(args[1]));

        Subscriber multicastClient = new Subscriber();
        Publisher.SubThread subThread = new Publisher.SubThread( multicastClient, args[0]+" client");

        subThread.start();
        pubThread.start();
    }
}
