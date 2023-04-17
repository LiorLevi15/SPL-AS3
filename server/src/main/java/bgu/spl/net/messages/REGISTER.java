package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class REGISTER extends Message<DataBase> {
    private String username;
    private String password;
    private String birthday;

    public REGISTER(String username, String password, String birthday) {
        super((short) 1);
        this.username=username;
        this.password=password;
        this.birthday=birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public Message execute(DataBase dataBase, int connectionId) {
        return dataBase.register(this, connectionId);
    }
}
