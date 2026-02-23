package com.priyanshparekh.ping.message;

import com.priyanshparekh.ping.chat.Chat;
import com.priyanshparekh.ping.chat.ChatRepository;
import com.priyanshparekh.ping.message.dto.ChatMessageListResponse;
import com.priyanshparekh.ping.message.dto.ChatMessageResponse;
import com.priyanshparekh.ping.message.dto.SaveMessageDto;
import com.priyanshparekh.ping.security.PingUserDetails;
import com.priyanshparekh.ping.user.User;
import com.priyanshparekh.ping.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public Message saveMessage(SaveMessageDto saveMessageDto) {
        Chat chat = chatRepository.findById(saveMessageDto.getChatId()).get();

        User user = userRepository.findById(saveMessageDto.getSenderId()).get();

        Message message = Message.builder()
                .text(saveMessageDto.getText())
                .chat(chat)
                .user(user)
                .createdAt(saveMessageDto.getCreatedAt())
                .chatMessageStatus(ChatMessageStatus.SENT)
                .build();

        Message savedMessage = messageRepository.save(message);

        log.info("Message_Service saveMessage: savedMessage id: {}", savedMessage.getId());

        return savedMessage;
    }

    public ChatMessageListResponse getMessages(Long chatId) {

        Long userId = ((PingUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();

        List<Message> messages = messageRepository.findAllByChatId(chatId);

        List<ChatMessageResponse> messageList = messages.stream().map(message -> {
            boolean isSent = message.getUser().getId().equals(userId);
            log.info("messageService: getMessages: userId: {}", userId);
            log.info("messageService: getMessages: message.getUser().getId(): {}", message.getUser().getId());
            log.info("messageService: getMessages: isSent: {}", isSent);
            return ChatMessageResponse.builder()
                    .message(message.getText())
                    .messageId(message.getId())
                    .senderId(message.getUser().getId())
                    .chatId(chatId)
                    .chatMessageStatus(message.getChatMessageStatus())
                    .build();
        }).toList();

        return ChatMessageListResponse.builder().messageList(messageList).build();
    }

    public void updateMessageStatus(Long messageId, ChatMessageStatus newStatus) {
        Message message = messageRepository.findById(messageId).get();

        ChatMessageStatus oldStatus = message.getChatMessageStatus();
        if (oldStatus.equals(ChatMessageStatus.READ)) {
            return;
        }

        if (oldStatus.equals(ChatMessageStatus.DELIVERED) && newStatus.equals(ChatMessageStatus.SENT)) {
            return;
        }

        message.setChatMessageStatus(newStatus);

        messageRepository.save(message);
    }
}
