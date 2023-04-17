package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;
import java.util.LinkedList;

public class POST extends Message<DataBase> {

    private String content;
    private LinkedList<String> tags;
    private String posterUser;

    public POST(String content, LinkedList<String> tags) {
        super((short) 5);
        this.content = content;
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setPosterUser(String posterUser) { this.posterUser = posterUser; }

    public String getPosterUser() { return posterUser; }

    public LinkedList<String> getTags() { return tags; }

    @Override
    public Message execute(DataBase dataBase, int connectionId) {
        return dataBase.post(this, connectionId);
    }
}
