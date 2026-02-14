package com.priyanshparekh.ping.chat;

import com.priyanshparekh.ping.chat.dto.AddChatRequest;
import com.priyanshparekh.ping.chat.dto.GetChatResponse;
import com.priyanshparekh.ping.chat.dto.ChatListResponse;
import com.priyanshparekh.ping.chatparticipant.ChatParticipant;
import com.priyanshparekh.ping.chatparticipant.ChatParticipantRepository;
import com.priyanshparekh.ping.chat.projection.ChatListProjection;
import com.priyanshparekh.ping.security.PingUserDetails;
import com.priyanshparekh.ping.user.User;
import com.priyanshparekh.ping.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public List<ChatParticipant> getChatParticipants(Long chatId) {
        return chatParticipantRepository.findAllByChatId(chatId);
    }

    @Transactional
    public void addChat(AddChatRequest addChatRequest) {
        User user1 = ((PingUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();


        Optional<Chat> existingChat = chatParticipantRepository.findOneToOneChat(user1.getId(), addChatRequest.getUserId());

        if (existingChat.isPresent()) {
            return;
        }


        Chat newChat = Chat.builder()
                .createdAt(System.currentTimeMillis())
                .build();
        Chat newChatAdded = chatRepository.save(newChat);


        ChatParticipant chatParticipant1 = ChatParticipant.builder()
                .chat(newChatAdded)
                .user(user1)
                .joinedAt(newChatAdded.getCreatedAt())
                .build();

        User user2 = userRepository.findById(addChatRequest.getUserId()).get();
        ChatParticipant chatParticipant2 = ChatParticipant.builder()
                .chat(newChatAdded)
                .user(user2)
                .joinedAt(newChatAdded.getCreatedAt())
                .build();

        chatParticipantRepository.saveAll(List.of(chatParticipant1, chatParticipant2));
    }


    public List<GetChatResponse> getAllChats() {
        User user = ((PingUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        return chatRepository.findAllChats(user.getId()).stream().map(chatProjection -> {
            log.info("chatService: getAllChats: chat id: {}", chatProjection.getId());
            return GetChatResponse.builder()
                    .chatId(chatProjection.getId())
                    .name(chatProjection.getName())
                    .build();
        }).toList();
    }
}
