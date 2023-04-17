package bgu.spl.net.messages;
import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class PM extends Message<DataBase> {

    private String username;
    private String content;
    private String time;
    private String senderUsername;

    public PM(String username, String content, String time) {
        super((short) 6);
        this.content=content;
        this.time=time;
        this.username=username;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }

    public String getSenderUsername() { return senderUsername; }

    @Override
    public Message execute(DataBase dataBase, int connectionId) {
        return dataBase.sendPM(this, connectionId);
    }
}
