package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class ERROR extends Message<DataBase> {
    private short msgOpcode;

    public ERROR(short msgOpcode) {
        super((short) 11);
        this.msgOpcode=msgOpcode;
    }

    @Override
    //TODO: complete
    public Message execute(DataBase dataBase, int connectionId) {
        return null;
    }

    public short getMsgOpcode() {
        return msgOpcode;
    }

}
