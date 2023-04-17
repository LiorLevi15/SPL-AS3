package bgu.spl.net.api.bidi;

import bgu.spl.net.DataBase;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.messages.*;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class EncoderDecoder implements MessageEncoderDecoder<Message> {
    private LinkedList<Byte> bytesList;
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    int index;
    int opcode=0;

    public EncoderDecoder(){
        bytesList=new LinkedList<Byte>();
        index=-1;
    }

    public byte[] objectsToBytes(Object[] arr) { // Why the heck is it difficult to cast an Object array to a byte array
        byte[] ret = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ret[i] = (byte)arr[i];
        }
        return ret;
    }

    //I wont to read.
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == ';') {
            String msg = popString();
            //
            String[] bytesArr = new String[msg.length()];
            for(int i=0; i<msg.length(); i++){
                bytesArr[i]=String.valueOf(msg.charAt(i));
            }
            if(bytesArr[0].equals("0") & bytesArr[1].equals("1")){//REGISTER
                index=2;
                String userName = nextWord(bytesArr);
                String password = nextWord(bytesArr);;
                String birthday = nextWord(bytesArr);
                return new REGISTER(userName,password,birthday);

            }if(bytesArr[0].equals("0") & bytesArr[1].equals("2")){//LOGIN
                index=2;
                String userName = nextWord(bytesArr);
                String password = nextWord(bytesArr);
                //System.out.println("CAPCHA IS : "+Character.toString(bytesArr[index].charAt(0)));
                return new LOGIN(userName,password,Byte.parseByte(Character.toString(bytesArr[index].charAt(0))));
            }if(bytesArr[0].equals("0") & bytesArr[1].equals("3")){//LOGOUT
                return new LOGOUT();
            }if(bytesArr[0].equals("0") & bytesArr[1].equals("4")){//FOLLOW
                index=4;
                String otherUsername = nextWord(bytesArr);
                return new FOLLOW(otherUsername,Byte.parseByte(bytesArr[2]));

            }if(bytesArr[0].equals("0") & bytesArr[1].equals("5")){//POST
                index=2;
                LinkedList<String> tags = new LinkedList<String>();
                String content = "";
                while(bytesArr.length>index){
                    String nextW=nextWord(bytesArr);
                    content=content+" "+nextW;
                    if(nextW.startsWith("@")) tags.add(nextW.substring(1));
                }
                return new POST(content,tags);
            }if((bytesArr[0].equals("0") & bytesArr[1].equals("6"))|| (bytesArr[0].equals("\0") && bytesArr[1].equals("0") && bytesArr[2].equals("6"))){//PM
                 LinkedList<String> toFilter = new LinkedList<String>();
                 toFilter.add("WAR");
                toFilter.add("TRUMP");
                index=2;
                if (bytesArr[0].equals("\0")) index++;
                String content ="";
                String userName = nextWord(bytesArr);
                String tempWord;
                while(bytesArr.length>index){
                    tempWord= nextWord(bytesArr);
                    if(toFilter.contains(tempWord.toUpperCase(Locale.ROOT))) tempWord="filtered";
                    content = content+" "+tempWord;
                }
                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                return new PM(userName,content,date);
            }if(bytesArr[0].equals("0") & bytesArr[1].equals("7")){//LOGSTAT
                return new LOGSTAT();
            }if(bytesArr[0].equals("0") & bytesArr[1].equals("8")){//STAT
                LinkedList<String> userNamesList = new LinkedList<String>();
                index=2;
                while (index<bytesArr.length) {
                //while(!bytesArr[index].equals("0")){
                    userNamesList.add(nextWord2(bytesArr));
                }
                Object[] userNamesArrObject = userNamesList.toArray();
                String[] userNamesArrString = new String[userNamesArrObject.length];
                int i=0;
                for( Object userName : userNamesArrObject){
                    userNamesArrString[i]=(String)userNamesArrObject[i];
                    i++;
                }
                return new STAT(userNamesArrString);
            }if(bytesArr[0].equals("1") & bytesArr[1].equals("2")) {//BLOCK
                index = 2;
                String userName = nextWord(bytesArr);
                return new BLOCK(userName);
            }
        } else pushByte(nextByte);
        return null; //not a line yet
    }

    private String nextWord2(String[] bytesArr){
        String ans = "";

        while (index<bytesArr.length && !(bytesArr[index].equals("|")) ){
            ans=ans+bytesArr[index];
            index++;
        }
        index++;
        return ans;
    }

    private String nextWord(String[] bytesArr){
        String ans = "";

        while (index<bytesArr.length && !(bytesArr[index].equals("\0")) ){
            ans=ans+bytesArr[index];
            index++;
        }
        index++;
        return ans;
    }
    //I wont to send.
    public byte[] encode(Message message){
        String messageString="";
        if(message.getOpcode()==9){//NOTIFICATION
            messageString="09";
            NOTIFICATION notification = (NOTIFICATION) message;
            messageString=messageString+notification.getPMOrPublic()+notification.getPostingUser()+notification.getContent();
        } else if (message.getOpcode()==10){//ACK
            messageString="10";
            ACK ack = (ACK) message;
            //TODO - optional;
            messageString=messageString+ack.getMsgOpcode();
            if (ack.getMsgOpcode() == 7 || ack.getMsgOpcode() == 8) {
                Object[] attachments = ack.getAttachment();
                for (Object attachment : attachments)
                    messageString += " "+attachment;
//                messageString = messageString.substring(0, messageString.length()-1);
            }
            else if (ack.getMsgOpcode() == 4) {
                String username = (String) ack.getAttachment()[0];
                messageString += " "+username;
            }

        } else if(message.getOpcode()==11){//ERROR
            messageString="11";
            ERROR error = (ERROR) message;
            messageString=messageString+error.getMsgOpcode();

        }
        messageString=messageString+";";
        byte[] bytes = messageString.getBytes(StandardCharsets.UTF_8);
        return bytes;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
}


