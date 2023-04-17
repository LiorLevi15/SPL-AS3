package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class STAT extends Message<DataBase> {

    private String[] usernames;

    public STAT(String[] usernames) {
        super((short) 8);
        this.usernames = usernames;
    }

    public String[] getUsernames() {
        return usernames;
    }

    @Override
    public Message execute(DataBase dataBase, int connectionId) {
        return dataBase.stat(this, connectionId);
    }
}
