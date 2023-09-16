package com.junhyeong.chatchat.config;

import com.junhyeong.chatchat.interceptors.ChatWebSocketHandler;
import com.junhyeong.chatchat.interceptors.CustomHandshakeInterceptor;
import com.junhyeong.chatchat.repositories.session.SessionRepository;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final SessionRepository sessionRepository;

    public WebSocketConfig(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/messages")
                .setAllowedOrigins("https://client.chatchat-web.site/",
                        "https://localhost:8080")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Bean
    public ChatWebSocketHandler chatWebSocketHandler() {
        return new ChatWebSocketHandler(sessionRepository);
    }

    @Bean
    public CustomHandshakeInterceptor customHandshakeInterceptor(JwtUtil jwtUtil) {
        return new CustomHandshakeInterceptor(jwtUtil);
    }
}
