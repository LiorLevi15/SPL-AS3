package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class BLOCK extends Message<DataBase> {

    private String otherUsername;

    public BLOCK(String username) {
        super((short) 12);
        this.otherUsername=username;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    @Override
    public Message execute(DataBase dataBase, int connectionId) {
        return dataBase.block(this, connectionId);
    }
}
