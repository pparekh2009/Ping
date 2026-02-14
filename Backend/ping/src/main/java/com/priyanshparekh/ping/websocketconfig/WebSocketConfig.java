package com.priyanshparekh.ping.websocketconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws/chat")
                .addInterceptors(handshakeInterceptor())
                .setAllowedOrigins("*");
    }


    @Bean
    WebSocketHandler webSocketHandler() {
        return new ChatWebSocketHandler();
    }

    @Bean
    HandshakeInterceptor handshakeInterceptor() {
        return new JwtHandshakeInterceptor();
    }

}
