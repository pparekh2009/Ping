package com.priyanshparekh.ping.websocketconfig;

import com.priyanshparekh.ping.chat.ChatService;
import com.priyanshparekh.ping.chatparticipant.ChatParticipant;
import com.priyanshparekh.ping.websocketconfig.event.ChatMessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketEventDispatcher {

    private final SessionRegistry sessionRegistry;

    private final ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    public void broadcastToChat(SocketEvent socketEvent, WebSocketSession session) {
        ChatMessagePayload chatMessagePayload = objectMapper.treeToValue(socketEvent.getPayload(), ChatMessagePayload.class);
        List<ChatParticipant> participants = chatService.getChatParticipants(chatMessagePayload.getChatId());

        Long senderId = (Long) session.getAttributes().get("userId");

        log.info("chatWebSocketHandler: handleTextMessage: participant size: {}", participants.size());
        participants.forEach(participant -> {
            log.info("chatWebSocketHandler: handleTextMessage: participant id: {}", participant.getId());
            log.info("chatWebSocketHandler: handleTextMessage: participant name: {}", participant.getUser().getName());
        });

        for (ChatParticipant participant : participants) {
            if (!participant.getUser().getId().equals(senderId)) {
                try {
                    sendToUser(participant.getUser().getId(), socketEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void sendToUser(Long userId, SocketEvent eventPayload) throws IOException {
        Set<WebSocketSession> userSessions = sessionRegistry.getAllSessions().get(userId);
        String payloadString = objectMapper.writeValueAsString(eventPayload);
        TextMessage textMessage = new TextMessage(payloadString);
        for (WebSocketSession session : userSessions) {
            if (session.isOpen()) {
                session.sendMessage(textMessage);
                log.info("chatWebSocketHandler: sendToUser: Message sent");
            }
        }
    }


//    public void broadcastToParticipants(SocketEvent socketEvent, WebSocketSession session) {
//        ChatMessagePayload chatMessagePayload = objectMapper.treeToValue(socketEvent.getPayload(), ChatMessagePayload.class);
//        List<ChatParticipant> participants = chatService.getChatParticipants(chatMessagePayload.getChatId());
//
//        Long senderId = (Long) session.getAttributes().get("userId");
//
//        for (ChatParticipant participant : participants) {
//            if (!participant.getUser().getId().equals(senderId)) {
//                try {
//                    sendToUser(participant.getUser().getId(), socketEvent);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
//
//    private void sendToUsers(Long userId, SocketEvent eventPayload) throws IOException {
//        Set<WebSocketSession> userSessions = sessionRegistry.getAllSessions().get(userId);
//        String payloadString = objectMapper.writeValueAsString(eventPayload);
//        TextMessage textMessage = new TextMessage(payloadString);
//        for (WebSocketSession session : userSessions) {
//            if (session.isOpen()) {
//                session.sendMessage(textMessage);
//                log.info("chatWebSocketHandler: sendToUser: Message sent");
//            }
//        }
//    }
}
