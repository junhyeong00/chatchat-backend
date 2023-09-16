package com.junhyeong.chatchat.interceptors;

import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.session.SessionRepository;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    private SessionRepository sessionRepository;

    public ChatWebSocketHandler(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long chatRoomId = (Long) session.getAttributes().get("chatRoomId");

        Username username = (Username) session.getAttributes().get("username");

        sessionRepository.addSession(chatRoomId, session.getId() , username);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long chatRoomId = (Long) session.getAttributes().get("chatRoomId");

        sessionRepository.removeSession(chatRoomId, session.getId());
    }
}
