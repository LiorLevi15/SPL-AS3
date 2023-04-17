package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class NOTIFICATION extends Message<DataBase> {

    private byte PMOrPublic;
    private String postingUser;
    public String content;

    public NOTIFICATION(byte PMOrPublic, String postingUser, String content) {
        super((short) 9);
        this.PMOrPublic=PMOrPublic;
        this.postingUser=postingUser;
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    public byte getPMOrPublic() {
        return PMOrPublic;
    }

    public String getPostingUser() {
        return postingUser;
    }

    @Override
    //TODO: complete
    public Message execute(DataBase dataBase, int connectionId) {
        return null;
    }
}
