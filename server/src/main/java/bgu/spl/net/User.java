package bgu.spl.net;

import bgu.spl.net.messages.PM;
import bgu.spl.net.messages.POST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
//fghghghgfdfghjlkkkjhgfds
public class User {

    private String name;
    private String password;
    private String birthday;
    private  boolean isLogIn;
    private int connectionId;
    private final ConcurrentHashMap<String, User> following;
    private final ConcurrentHashMap<String, User> followers;
    private final ConcurrentHashMap<String, User> blocking;
    private final ConcurrentLinkedQueue<POST> posts;
    private final ConcurrentLinkedQueue<PM> pms;
    private final ConcurrentLinkedQueue<POST> awaitingPosts;
    private final ConcurrentLinkedQueue<PM> awaitingPMs;
    //TODO:

    public User(int connectionId, String name, String password, String birthday){
        this.name=name;
        this.password=password;
        this.birthday = birthday;
        this.connectionId = connectionId;
        followers = new ConcurrentHashMap<>();
        following = new ConcurrentHashMap<>();
        blocking = new ConcurrentHashMap<>();
        posts = new ConcurrentLinkedQueue<>();
        pms = new ConcurrentLinkedQueue<>();
        awaitingPosts = new ConcurrentLinkedQueue<>();
        awaitingPMs = new ConcurrentLinkedQueue<>();
        isLogIn = false;
    }

    public String getPassword() { return password; }

    public String getName() { return name;}

    public String getBirthday() { return birthday; }

    public int getConnectionId() { return connectionId; }

    public void setConnectionId(int connectionId) { this.connectionId = connectionId; }

    public void LogIn(){ isLogIn = true; }

    public void LogOut(){isLogIn=false;}

    public boolean isLogIn() {return isLogIn;}

    public void follow(User user){ following.put(user.getName(), user); }

    public void unFollow(User user){ following.remove(user.getName()); }

    public void followME(User user){
        followers.put(user.getName(), user);
    }

    public void unFollowME(User user){
        followers.remove(user.getName());
    }

    public boolean isFollowing(User otherUser){
        return following.containsKey(otherUser.getName());
    }

    public Collection<User> getFollowers() { return  followers.values(); }

    public int getNumOfFollowers() { return followers.size(); }

    public int getNumOfFollowing() { return  following.size(); }

    public void block(User otherUser) { blocking.put(otherUser.getName(), otherUser); }

    public boolean isBlocking(User otherUser) { return blocking.containsKey(otherUser.getName()); }

    public void addPost(POST post) { posts.add(post); }

    public void addPM(PM pm) { pms.add(pm); }

    public int getNumOfPost(){
        return posts.size();
    }

    public void addAwaitingPM(PM pm) { awaitingPMs.add(pm); }

    public void addAwaitingPost(POST post) { awaitingPosts.add(post); }

    public ConcurrentLinkedQueue<PM> getAwaitingPMs() { return awaitingPMs;}

    public ConcurrentLinkedQueue<POST> getAwaitingPosts() { return awaitingPosts; }

    public int getAge() {
        Calendar now = Calendar.getInstance();
        Calendar birthdate = Calendar.getInstance();
        Date birthdateDate = null;
        try {
            birthdateDate = new SimpleDateFormat("dd-MM-yyyy").parse(birthday);
        } catch (ParseException e) {}
        birthdate.setTime(birthdateDate);
        int year1 = now.get(Calendar.YEAR);
        int year2 = birthdate.get(Calendar.YEAR);
        int age = year1 - year2;
        int month1 = now.get(Calendar.MONTH);
        int month2 = birthdate.get(Calendar.MONTH);
        if (month2 > month1) {
            age--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = birthdate.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age--;
            }
        }
    return  age;

    }

}
