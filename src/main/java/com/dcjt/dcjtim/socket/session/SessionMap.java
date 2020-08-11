package com.dcjt.dcjtim.socket.session;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * session store
 * Created by 唐文滔 on 2020/6/1
 */
@Data
@Slf4j
public class SessionMap {

    public static SessionMap inst = SingleInstance.singleInstance;

    /**
     * 用户通道
     */
    private ConcurrentHashMap<String, Set<SocketIOClient>> sessions = new ConcurrentHashMap<>();

    private static class SingleInstance {
        private static SessionMap singleInstance = new SessionMap();
    }

    public void addSession(String token,SocketIOClient client) {
        if (sessions.containsKey(token)) {
            sessions.get(token).add(client);
        } else {
            Set<SocketIOClient> clients = new HashSet<>();
            clients.add(client);
            sessions.put(token, clients);
        }
    }

    public Set<SocketIOClient> getSession(String token){
        return sessions.getOrDefault(token,null);
    }

    public void removeSession(String token,SocketIOClient client) {
        sessions.get(token).remove(client);
        if (sessions.get(token).isEmpty()) {
            sessions.remove(token);
        }
    }
}
