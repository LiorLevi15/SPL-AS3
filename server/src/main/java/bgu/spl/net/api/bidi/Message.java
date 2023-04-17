package bgu.spl.net.api.bidi;

public abstract class Message<DataBase> {
    protected short opcode;

    public Message(short opcode) {
        this.opcode = opcode;
    }

    public short getOpcode() {
        return opcode;
    }

    public abstract Message execute(DataBase dataBase, int connectionId);
}
