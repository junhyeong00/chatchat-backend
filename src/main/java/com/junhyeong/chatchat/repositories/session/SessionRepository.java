package com.junhyeong.chatchat.repositories.session;

import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class SessionRepository {
    private final Map<Long, Set<String>> chatRoomSessions = new HashMap<>();
    private final Map<String, Username> connectedUsers = new HashMap<>();

    public void addSession(Long chatRoomId, String sessionId, Username username) {
        chatRoomSessions.computeIfAbsent(chatRoomId, key -> new HashSet<>()).add(sessionId);

        connectedUsers.put(sessionId, username);
    }

    public void removeSession(Long chatRoomId, String sessionId) {
        chatRoomSessions.getOrDefault(chatRoomId, Collections.emptySet()).remove(sessionId);

        connectedUsers.remove(sessionId);
    }

    public boolean isSessionConnected(Long chatRoomId, String sessionId) {
        return chatRoomSessions.getOrDefault(chatRoomId, Collections.emptySet()).contains(sessionId);
    }

    public Set<String> getSessionIdByChatRoomId(Long chatRoomId) {
        return chatRoomSessions.get(chatRoomId);
    }

    public Username getUsernameBySessionId(String sessionId) {
        return connectedUsers.get(sessionId);
    }
}
