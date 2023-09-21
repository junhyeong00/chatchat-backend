package com.junhyeong.chatchat.interceptors;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.junhyeong.chatchat.applications.notification.MessageNotificationService;
import com.junhyeong.chatchat.exceptions.AuthenticationError;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.session.SessionRepository;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class ChatWebSocketHandler implements ChannelInterceptor {
    private static final Logger log = LoggerFactory.getLogger(MessageNotificationService.class);
    private SessionRepository sessionRepository;
    private JwtUtil jwtUtil;

    public ChatWebSocketHandler(SessionRepository sessionRepository, JwtUtil jwtUtil) {
        this.sessionRepository = sessionRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            log.info("[web socket] - preSend 메서드 /connect / 시작");

            String authorization = accessor.getFirstNativeHeader("Authorization");
            log.info("authorization: " + authorization);

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new AuthenticationError();
            }

            String accessToken = authorization.substring("Bearer ".length());

            try {
                Username username = jwtUtil.decode(accessToken);
                log.info("username: " + username.value());

                String requestUri = accessor.getDestination();
                log.info("requestUri: " + requestUri);

                Long chatRoomId = Long.valueOf(requestUri.split("chatrooms/")[1]);

                String sessionId = accessor.getSessionId();
                log.info("sessionId: " + sessionId);

                sessionRepository.addSession(chatRoomId, sessionId, username);

                log.info("[web socket] - preSend 메서드 / connect / 완료");
                return message;
            } catch (JWTDecodeException exception) {
                throw new AuthenticationError();
            }
        }

        if (accessor.getCommand() == StompCommand.DISCONNECT) {
            log.info("[web socket] - preSend 메서드 / disconnect / 시작");

            String requestUri = accessor.getFirstNativeHeader("host");
            log.info("requestUri: " + requestUri);

            Long chatRoomId = Long.valueOf(requestUri.split("chatrooms/")[1]);

            String sessionId = accessor.getSessionId();
            log.info("sessionId: " + sessionId);

            sessionRepository.removeSession(chatRoomId, sessionId);

            log.info("[web socket] - preSend 메서드 / disconnect / 끝");
        }

        return message;
    }
}
