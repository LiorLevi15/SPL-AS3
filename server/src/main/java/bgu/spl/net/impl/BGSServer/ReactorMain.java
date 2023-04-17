package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.EncoderDecoder;
import bgu.spl.net.srv.TwitterProtocol;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main (String[] args) {

        int port = Integer.parseInt(args[0]);
        int numOfThreads = Integer.parseInt(args[1]);
//        int port = 7777;
//        int numOfThreads = 2;
        Server.reactor(numOfThreads,
                port,
                () -> new TwitterProtocol(),
                EncoderDecoder::new
        ).serve();
    }
}
