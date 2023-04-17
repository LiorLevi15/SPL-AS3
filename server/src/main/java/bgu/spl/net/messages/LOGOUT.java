package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class LOGOUT extends Message<DataBase> {

    public LOGOUT() {
        super((short) 3);
    }

    @Override
    public Message execute(DataBase dataBase, int connectionId) {
        return dataBase.logout(this, connectionId);

    }

}
