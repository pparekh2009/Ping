package com.priyanshparekh.ping.websocketconfig.handler;

import com.priyanshparekh.ping.chat.ChatService;
import com.priyanshparekh.ping.chatparticipant.ChatParticipant;
import com.priyanshparekh.ping.message.Message;
import com.priyanshparekh.ping.message.MessageService;
import com.priyanshparekh.ping.message.dto.SaveMessageDto;
import com.priyanshparekh.ping.websocketconfig.SessionRegistry;
import com.priyanshparekh.ping.websocketconfig.SocketEvent;
import com.priyanshparekh.ping.websocketconfig.SocketEventDispatcher;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatMessageHandler {

    private final MessageService messageService;

    private final SocketEventDispatcher socketEventDispatcher;

    @Autowired
    private ObjectMapper mapper;

    public void handle(SocketEvent eventPayload, WebSocketSession session) {

        log.info("chatMessageHandler: handle: eventPayload: {}", eventPayload.toString());

        ChatMessagePayload chatMessagePayload = mapper.treeToValue(eventPayload.getPayload(), ChatMessagePayload.class);

        log.info("chatMessageHandler: handle: chatMessagePayload: {}", chatMessagePayload.toString());

        Message savedMessage = saveChatMessage(chatMessagePayload);
        chatMessagePayload.setMessageId(savedMessage.getId());

        eventPayload.setPayload(mapper.readTree(mapper.writeValueAsString(chatMessagePayload)));

        socketEventDispatcher.broadcastToChat(eventPayload, session);
    }

    private Message saveChatMessage(ChatMessagePayload payload) {

        SaveMessageDto saveMessageDto = SaveMessageDto.builder()
                .text(payload.getMessage())
                .chatId(payload.getChatId())
                .senderId(payload.getSenderId())
                .createdAt(System.currentTimeMillis())
                .build();
        return messageService.saveMessage(saveMessageDto);
    }
}
