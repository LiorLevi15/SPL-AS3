package bgu.spl.net.messages;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.bidi.Message;

public class LOGIN extends Message<DataBase> {

    private String username;
    private String password;
    private byte captcha;

    public LOGIN(String username, String password, byte captcha) {
        super((short) 2);
        this.username=username;
        this.password=password;
        this.captcha=captcha;
    }

    public byte getCaptcha() {
        return captcha;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public Message execute(DataBase dataBase, int connectionId) {
        return dataBase.login(this, connectionId, captcha);
    }
}
