
package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class ACK extends Message<DataBase> {
    private Object[] attachment;
    private short msgOpcode;


    public ACK(short msgOpcode, Object[] attachment) {
        super((short) 10);
        this.msgOpcode = msgOpcode;
        this.attachment = attachment;
    }

    public short getMsgOpcode() {
        return msgOpcode;
    }

    public Object[] getAttachment() {
        return attachment;
    }

    @Override
    //TODO: complete
    public Message execute(DataBase dataBase, int connectionId) {
        return null;
    }
}
