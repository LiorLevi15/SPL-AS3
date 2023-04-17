package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class FOLLOW extends Message<DataBase> {
    private String otherUsername;
    private byte fOrUF;

    public FOLLOW(String otherUsername, byte fOrUF) {
        super((short) 4);
        this.otherUsername=otherUsername;
        this.fOrUF=fOrUF;
    }

    public byte getFOrUF() {
        return fOrUF;
    }


    public String getOtherUsername(){
        return otherUsername;
    }

    @Override
    public Message execute(DataBase dataBase, int connectionId) {
        return dataBase.follow(this, connectionId);
    }
}
