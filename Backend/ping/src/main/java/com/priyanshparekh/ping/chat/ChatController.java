package com.priyanshparekh.ping.chat;

import com.priyanshparekh.ping.TextResponse;
import com.priyanshparekh.ping.chat.dto.AddChatRequest;
import com.priyanshparekh.ping.chat.dto.ChatListResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<@NonNull TextResponse> addChat(@RequestBody AddChatRequest addChatRequest) {
        chatService.addChat(addChatRequest);
        return ResponseEntity.ok(TextResponse.builder().message("Chat Added").build());
    }

    @GetMapping("/chats")
    public ResponseEntity<@NonNull ChatListResponse> getAllChats() {
        return ResponseEntity.ok(chatService.getAllChats());
    }
}
