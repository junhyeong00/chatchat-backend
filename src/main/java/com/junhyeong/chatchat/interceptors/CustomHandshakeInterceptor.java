package com.junhyeong.chatchat.interceptors;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.junhyeong.chatchat.exceptions.AuthenticationError;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    private JwtUtil jwtUtil;

    public CustomHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String authorization = request.getHeaders().getFirst("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return true;
        }

        String accessToken = authorization.substring("Bearer ".length());

        try {
            Username username = jwtUtil.decode(accessToken);

            attributes.put("username", username);

            String requestUri = request.getURI().toString();

            Long chatRoomId = Long.valueOf(requestUri.split("chatrooms/")[1]);

            attributes.put("chatRoomId", chatRoomId);

            return true;
        } catch (JWTDecodeException exception) {
            throw new AuthenticationError();
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {

    }
}
