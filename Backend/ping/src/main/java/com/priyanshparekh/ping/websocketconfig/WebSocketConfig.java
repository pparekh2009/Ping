package com.priyanshparekh.ping.websocketconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final PingWebSocketHandler pingWebSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws/chat")
                .addInterceptors(handshakeInterceptor())
                .setAllowedOrigins("*");
    }


    @Bean
    WebSocketHandler webSocketHandler() {
        return pingWebSocketHandler;
    }

    @Bean
    HandshakeInterceptor handshakeInterceptor() {
        return jwtHandshakeInterceptor;
    }

}
