package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T>{
    private static int nextConnectionId = 0;
    private ConcurrentHashMap<Integer, ConnectionHandler<T>> map;



    //singleton pattern
    private static class ConnectionHolder {
        private static ConnectionsImpl instance = new ConnectionsImpl();
    }
    private ConnectionsImpl() {
        this.map = new ConcurrentHashMap<>();
    }
    public static ConnectionsImpl  getInstance() {
        return ConnectionHolder.instance;
    }

    @Override
    public boolean send(int connectionId, T msg) {
        ConnectionHandler<T> hendler = map.get(connectionId);
        if (hendler != null && msg != null) {
            hendler.send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(T msg) {
        map.forEach((id, ch)-> {
            ch.send(msg);
        });
    }

    @Override
    public void disconnect(int connectionId) {
        map.remove(connectionId);
    }

    public void addHandler(int connectionId, ConnectionHandler<T> handler) {
        map.put(connectionId, handler);
    }

    public static int getAndIncrementId() {
        nextConnectionId++;
        return nextConnectionId;
    }

}
