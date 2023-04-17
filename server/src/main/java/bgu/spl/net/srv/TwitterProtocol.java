package bgu.spl.net.srv;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Message;

public class TwitterProtocol implements BidiMessagingProtocol<Message> {
    private boolean shouldTerminate = false;
    private Connections<Message> connections;
    private DataBase dataBase;
    private int connectionId;

    public TwitterProtocol() {
        dataBase = DataBase.getInstance();
    }

    @Override
    //TODO: add call to start
    public void start(int connectionId, Connections connections) {
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void process(Message message) {
        Message response = message.execute(dataBase, connectionId);
        if (response != null) {
            connections.send(connectionId, response);
            if (response.getOpcode() == (short) 10 & message.getOpcode() == (short)3 ) terminate();
        }
    }

    @Override
    public boolean shouldTerminate() { return shouldTerminate; }

    public void terminate(){
        shouldTerminate=true;
    }

}
