package com.priyanshparekh.ping.websocketconfig;

import com.priyanshparekh.ping.chat.ChatService;
import com.priyanshparekh.ping.chatparticipant.ChatParticipant;
import com.priyanshparekh.ping.message.MessageService;
import com.priyanshparekh.ping.websocketconfig.event.ChatMessagePayload;
import com.priyanshparekh.ping.websocketconfig.handler.ChatMessageHandler;
import com.priyanshparekh.ping.websocketconfig.handler.ChatStatusHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class PingWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final MessageService messageService;
    private final SessionRegistry sessionRegistry;

    private final ChatMessageHandler chatMessageHandler;
    private final ChatStatusHandler chatStatusHandler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        log.info("Connected: {}", session.getId());

        Long userId = (Long) session.getAttributes().get("userId");
        log.info("pingWebSocketHandler: afterConnectionEstablished: userId: {}", userId);

        sessionRegistry.addSession(userId, session);

        sessionRegistry.getAllSessions().keySet().forEach(key -> {
            Set<WebSocketSession> sessionSet = sessionRegistry.getAllSessions().get(key);
            sessionSet.forEach(webSocketSession -> {
                log.info("pingWebSocketHandler: afterConnectionEstablished: webSocketSession Id: {}", webSocketSession.getId());
            });
        });
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        log.info("Received: {}", message.getPayload());

        SocketEvent payload = objectMapper.readValue(message.getPayload(), SocketEvent.class);

        switch (payload.getSocketEventType()) {
            case CHAT_MESSAGE -> {
                chatMessageHandler.handle(payload, session);
//                sendToParticipants(event, session);
            }
            case CHAT_STATUS -> chatStatusHandler.handle(payload, session);
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        log.info("Disconnected: session id: {}", session.getId());
        log.info("Disconnected: close status code: {}", closeStatus.getCode());

        Long userId = (Long) session.getAttributes().get("userId");

        sessionRegistry.removeSession(userId, session);
    }
}
