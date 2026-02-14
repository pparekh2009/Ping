package com.priyanshparekh.ping.message;

import com.priyanshparekh.ping.chat.Chat;
import com.priyanshparekh.ping.chat.ChatRepository;
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

    public void saveMessage(SaveMessageDto saveMessageDto) {
        Chat chat = chatRepository.findById(saveMessageDto.getChatId()).get();

        User user = userRepository.findById(saveMessageDto.getSenderId()).get();

        Message message = Message.builder()
                .text(saveMessageDto.getText())
                .chat(chat)
                .user(user)
                .createdAt(saveMessageDto.getCreatedAt())
                .build();

        messageRepository.save(message);
    }

    public List<MessageHistoryResponse> getMessages(Long chatId) {

        Long userId = ((PingUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();

        List<Message> messages = messageRepository.findAllByChatId(chatId);
        return messages.stream().map(message -> {
            boolean isSent = message.getUser().getId().equals(userId);
            log.info("messageService: getMessages: userId: {}", userId);
            log.info("messageService: getMessages: message.getUser().getId(): {}", message.getUser().getId());
            log.info("messageService: getMessages: isSent: {}", isSent);
            return new MessageHistoryResponse(message.getText(), message.getUser().getId().equals(userId));
        }).toList();
    }

}
