package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.EncoderDecoder;
import bgu.spl.net.srv.TwitterProtocol;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main (String[] args){

        int port = Integer.parseInt(args[0]);
//        int port = 7777;
        Server.threadPerClient(port,
                                () -> new TwitterProtocol(),
                                () ->new EncoderDecoder()
        ).serve();
    }
}
