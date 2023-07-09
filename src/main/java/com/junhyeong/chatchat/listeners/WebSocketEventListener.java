package com.junhyeong.chatchat.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Connected");
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionSubscribeEvent event) {
        System.out.println(
                "Subscribed: "
                        + event.getMessage().getHeaders().get("simpDestination")
        );
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        System.out.println("Disconnected");
    }
}
