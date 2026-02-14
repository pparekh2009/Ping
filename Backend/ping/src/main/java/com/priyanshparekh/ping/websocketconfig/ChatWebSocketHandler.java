package com.priyanshparekh.ping.websocketconfig;

import com.priyanshparekh.ping.message.dto.SaveMessageDto;
import com.priyanshparekh.ping.message.dto.MessagePayload;
import com.priyanshparekh.ping.chat.ChatService;
import com.priyanshparekh.ping.chatparticipant.ChatParticipant;
import com.priyanshparekh.ping.message.MessageService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, Set<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        log.info("Connected: {}", session.getId());

        Long userId = (Long) session.getAttributes().get("userId");
        log.info("chatWebSocketHandler: afterConnectionEstablished: userId: {}", userId);

        sessions.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        sessions.keySet().forEach(key -> {
            Set<WebSocketSession> sessionSet = sessions.get(key);
            sessionSet.forEach(webSocketSession -> {
                log.info("chatWebSocketHandler: afterConnectionEstablished: webSocketSession Id: {}", webSocketSession.getId());
            });
        });
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        log.info("Received: {}", message.getPayload());

        Long senderId = (Long) session.getAttributes().get("userId");
        log.info("");

        MessagePayload payload = objectMapper.readValue(message.getPayload(), MessagePayload.class);

        SaveMessageDto saveMessageDto = SaveMessageDto.builder()
                .text(payload.getPayload())
                .chatId(payload.getChatId())
                .senderId(payload.getSenderId())
                .createdAt(System.currentTimeMillis())
                .build();
        messageService.saveMessage(saveMessageDto);

        List<ChatParticipant> participants = chatService.getChatParticipants(payload.getChatId());
        log.info("chatWebSocketHandler: handleTextMessage: participant size: {}", participants.size());
        participants.forEach(participant -> {
            log.info("chatWebSocketHandler: handleTextMessage: participant id: {}", participant.getId());
            log.info("chatWebSocketHandler: handleTextMessage: participant name: {}", participant.getUser().getName());
        });

        for (ChatParticipant participant : participants) {
            if (!participant.getUser().getId().equals(senderId)) {
                sendToUser(participant.getUser().getId(), message.getPayload());
            }
        }
    }

    void sendToUser(Long userId, String payload) throws IOException {
        Set<WebSocketSession> userSessions = sessions.get(userId);
        for (WebSocketSession session : userSessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(payload));
                log.info("chatWebSocketHandler: sendToUser: Message sent");
            }
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        log.info("Disconnected: session id: {}", session.getId());
        log.info("Disconnected: close status code: {}", closeStatus.getCode());

        Long userId = (Long) session.getAttributes().get("userId");

        Set<WebSocketSession> userSessions = sessions.get(userId);
        if (userSessions != null) {
            userSessions.remove(session);
            if (userSessions.isEmpty()) {
                sessions.remove(userId);
            }
        }
    }
}
