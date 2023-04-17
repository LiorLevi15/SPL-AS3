package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class LOGSTAT extends Message<DataBase> {

    public LOGSTAT() {
        super((short) 7);
    }

    @Override
    public Message execute(DataBase dataBase, int connectionId) {
        return dataBase.logStat(this, connectionId);
    }
}
