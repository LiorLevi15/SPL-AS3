package bgu.spl.net;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.messages.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {

    private final ConcurrentHashMap<Integer, User> users;
    private final ConcurrentHashMap<String, User> usernames;
    private ConnectionsImpl<Message> connections;

    //singleton pattern
    private static class DataBaseHolder {
        static final DataBase instance = new DataBase();
    }

    public static DataBase  getInstance() {
        return DataBaseHolder.instance;
    }

    private DataBase(){
        users = new ConcurrentHashMap<>();
        usernames = new ConcurrentHashMap<>();
        connections = ConnectionsImpl.getInstance();

    }
    public Message register(REGISTER register, int connectionId){
        if (usernames.containsKey(register.getUsername()))
            return error(register.getOpcode());
        else {
            User user = new User(connectionId, register.getUsername(), register.getPassword(), register.getBirthday());
            users.put(connectionId, user);
            usernames.put(user.getName(), user);
            return ack(register.getOpcode(), null);
        }
    }

    public Message login(LOGIN login, int connectionId, byte captcha){
        User user = usernames.get(login.getUsername());
        if(user != null && user.getPassword().equals(login.getPassword()) && !user.isLogIn() && ((int)captcha)!=0) {
            user.LogIn();
            user.setConnectionId(connectionId);
            users.put(connectionId, user);
            for (PM pm : user.getAwaitingPMs()) {
                sendNotification((byte) 0, user.getName(), pm.getContent(), connectionId);
            }
            for (POST post : user.getAwaitingPosts()) {
                sendNotification((byte) 1, user.getName(), post.getContent(), connectionId);
            }
            return ack(login.getOpcode(), null);
        }
        else {
            return error(login.getOpcode());
        }
    }

    public Message logout(LOGOUT logout, int connectionId){
        User user = users.get(connectionId);
        if(user != null && user.isLogIn()){
            user.LogOut();
            users.remove(connectionId);
            return ack(logout.getOpcode(), null);
        }
        else {
            return error(logout.getOpcode());
        }
    }

    public Message follow(FOLLOW follow, int connectionId) {
        User user = users.get(connectionId);
        User otherUser = usernames.get(follow.getOtherUsername());
        if (user == null || otherUser == null || !user.isLogIn()
                || ( follow.getFOrUF() == (byte) 0 && ( user.isFollowing(otherUser) || otherUser.isBlocking(user) || user.isBlocking(otherUser) ) )
                || ( follow.getFOrUF() == (byte) 1 && !user.isFollowing(otherUser) ) ) {
            return error(follow.getOpcode());
        }
        if (follow.getFOrUF() == (byte) 0) {
            user.follow(otherUser);
            otherUser.followME(user);
        }
        else {
            user.unFollow(otherUser);
            otherUser.unFollowME(user);
        }
        Object[] attachment = new Object[1];
        attachment[0] = otherUser.getName();
        return ack(follow.getOpcode(), attachment);
    }

    public Message block(BLOCK block, int connectionId) {
        User user = users.get(connectionId);
        User otherUser = usernames.get(block.getOtherUsername());
        if (user == null || otherUser == null || !user.isLogIn())
            return error(block.getOpcode());
        else {
            user.unFollow(otherUser);
            otherUser.unFollowME(user);
            otherUser.unFollow(user);
            user.unFollowME(otherUser);
            user.block(otherUser);
            return ack(block.getOpcode(), null);
        }
    }
    //TODO: complete
    public Message post(POST post, int connectionId) {
        User user = users.get(connectionId);
        if (user == null || !user.isLogIn())
            return error(post.getOpcode());
        else {
            Collection<User> followers = user.getFollowers();
            for (User otherUser : followers) {
                if (otherUser.isLogIn())
                    sendNotification((byte) 1, user.getName(), post.getContent(), otherUser.getConnectionId());
                else { otherUser.addAwaitingPost(post); }
            }
            for (String tag : post.getTags()) {
                User otherUser = usernames.get(tag);
                if (otherUser == null || otherUser.isBlocking(user) || user.isBlocking(otherUser))
                    continue;
                if (otherUser.isLogIn())
                    sendNotification((byte) 1, user.getName(), post.getContent(), otherUser.getConnectionId());
                else { otherUser.addAwaitingPost(post); }
            }
            user.addPost(post);
            return ack(post.getOpcode(), null);
        }
    }
    //TODO: complete
    public Message sendPM(PM pm, int connectionId) {
        User user = users.get(connectionId);
        if (user == null || !user.isLogIn())
            return error(pm.getOpcode());
        User otherUser = usernames.get(pm.getUsername());
        if (otherUser == null || !user.isFollowing(otherUser))
            return error(pm.getOpcode());
        if (otherUser.isLogIn())
            sendNotification((byte) 0, user.getName(), pm.getContent(), otherUser.getConnectionId());
        else { otherUser.addAwaitingPM(pm); }
        user.addPM(pm);
        return ack(pm.getOpcode(), null);
    }

    public Message logStat(LOGSTAT logStat, int connectionId) {
        User user = users.get(connectionId);
        if (user == null || !user.isLogIn())
            return error(logStat.getOpcode());
        Collection<User> usersCol = users.values();
        for (User otherUser : usersCol) {
            if (!otherUser.isBlocking(user) && !user.isBlocking(otherUser)) {
                LinkedList<Object> attachment = new LinkedList<>();
                attachment.add((short) otherUser.getAge());
                attachment.add((short) otherUser.getNumOfPost());
                attachment.add((short) otherUser.getNumOfFollowers());
                attachment.add((short) otherUser.getNumOfFollowing());
                sendACK(logStat.getOpcode(), attachment.toArray(), connectionId);
            }
        }
        return null;
    }

    public Message stat(STAT stat, int connectionId) {
        User user = users.get(connectionId);
        if (user == null || !user.isLogIn())
            return error(stat.getOpcode());
        for (String username : stat.getUsernames()) {
            User otherUser = usernames.get(username);
            if (otherUser == null || otherUser.isBlocking(user) || user.isBlocking(otherUser) )
                sendERROR(stat.getOpcode(), connectionId);
            else {
                LinkedList<Object> attachment = new LinkedList<>();
                attachment.add((short) otherUser.getAge());
                attachment.add((short) otherUser.getNumOfPost());
                attachment.add((short) otherUser.getNumOfFollowers());
                attachment.add((short) otherUser.getNumOfFollowing());
                sendACK(stat.getOpcode(), attachment.toArray(), connectionId);
            }
        }
        return null;
    }

    private void sendNotification(byte pmOrPublic, String postingUser, String content, int connectionId) {
        NOTIFICATION notification = new NOTIFICATION(pmOrPublic, postingUser, content);
        connections.send(connectionId, notification);
    }

    private void sendACK(short opcode, Object[] attachment, int connectionId) {
        ACK ack = new ACK(opcode, attachment);
        connections.send(connectionId, ack);
    }

    private void sendERROR(short opcode, int connectionId) {
        ERROR error = new ERROR(opcode);
        connections.send(connectionId, error);
    }

    private Message ack(short opcode, Object[] attachment) { return new ACK(opcode, attachment); }

    private ERROR error(short opcode) {
        return new ERROR(opcode);
    }

}
